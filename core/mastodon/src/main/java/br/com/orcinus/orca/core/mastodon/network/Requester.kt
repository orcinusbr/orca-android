/*
 * Copyright © 2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.mastodon.network

import androidx.annotation.VisibleForTesting
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.SomeAuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.network.client.Logger
import br.com.orcinus.orca.core.mastodon.network.client.normalizeJsonKeys
import br.com.orcinus.orca.core.mastodon.network.request.Authentication
import br.com.orcinus.orca.core.mastodon.network.request.Parameterization
import br.com.orcinus.orca.core.mastodon.network.request.Request
import br.com.orcinus.orca.core.mastodon.network.request.RequestDao
import br.com.orcinus.orca.core.mastodon.network.request.Resumption
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Parameters
import io.ktor.http.content.PartData
import java.net.URI
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Manages and automatically retries in case of failures HTTP requests that have been requested to
 * be performed, caching their results for saving of resources and effortless retrievals when an
 * internet connection isn't available.
 *
 * Requests can also be made resumable, which means that they will be automatically retried in case
 * this [Requester] is interrupted and gets resumed posteriorly. For more information, refer to the
 * documentation of [Resumption] and its policies'.
 *
 * @param authenticationLock [AuthenticationLock] for unlocking operations made by an
 *   unauthenticated [Actor].
 * @param clientEngineFactory [HttpClientEngineFactory] that creates the [HttpClientEngine] powering
 *   the underlying [client].
 * @param logger [Logger] by which received [HttpResponse]s will be logged.
 * @param requestDao [RequestDao] for performing read and write operations on [Request]s.
 * @param base [URI] from which routes are constructed.
 * @param scope Creates a new [CoroutineScope] in which the time to live of a performed request will
 *   be awaited.
 * @see Resumption.Resumable
 * @see interrupt
 * @see resume
 */
internal class Requester
@InternalNetworkApi
constructor(
  private val authenticationLock: SomeAuthenticationLock,
  private val clientEngineFactory: HttpClientEngineFactory<*>,
  private val logger: Logger,
  private val requestDao: RequestDao,
  private val base: URI,
  private val scope: () -> CoroutineScope
) {
  /** [HttpClient] that will be responsible for sending HTTP requests. */
  private val client =
    HttpClient(clientEngineFactory) {
      defaultRequest { url(base.scheme, base.host, base.port.takeIf { it >= 0 }, base.path) }
      logger.start(this)
      normalizeJsonKeys()
      retryAfterFailures()
    }

  /** Responses that are currently being obtained. */
  private val ongoing = hashMapOf<Request, Deferred<HttpResponse>>()

  /**
   * Responses to previously performed requests that haven't yet become stale (that is, are alive)
   * and are eligible for reuse.
   */
  private val cache = hashMapOf<Request, HttpResponse>()

  /**
   * [CancellationException] that is the cause of when a request is interrupted.
   *
   * @param request Metadata of the interrupted request.
   * @see Requester.interrupt
   */
  class InterruptionException @InternalNetworkApi internal constructor(request: Request) :
    CancellationException("$request has been interrupted by its requester.")

  /**
   * Restarts requests that have been attempted to be performed previously and were cancelled when
   * this [Requester] was last interrupted, whose information were persisted by the HTTP-analogous
   * method when it called [request].
   *
   * @see interrupt
   */
  suspend fun resume() {
    requestDao.selectAll().forEach {
      it.fold(
        onDelete = { delete(it.authentication, it.route, Resumption.Resumable) },
        onGet = { get(it.authentication, it.route, Resumption.Resumable) },
        onPost = {
          when (
            val parameterization = Parameterization.deserialize(it.parameterization, it.parameters)
          ) {
            is Parameterization.Body ->
              post(it.authentication, it.route, parameterization.content, Resumption.Resumable)
            is Parameterization.Headers ->
              post(it.authentication, it.route, parameterization.content, Resumption.Resumable)
          }
        }
      )
    }
  }

  /**
   * Sends an HTTP `DELETE` request.
   *
   * @param authentication Authentication requirement that is appropriate for this specific request.
   * @param route Route from the [base] to which the request will be sent.
   * @param resumption Policy for defining whether the request should be resumed in case it is
   *   interrupted.
   */
  suspend fun delete(
    authentication: Authentication,
    route: String,
    resumption: Resumption = Resumption.None
  ): HttpResponse {
    return request(
      authentication,
      Request.MethodName.DELETE,
      route,
      resumption,
      Parameterization.empty
    ) {
      client.delete(route) { it() }
    }
  }

  /**
   * Sends an HTTP `GET` request.
   *
   * @param authentication Authentication requirement that is appropriate for this specific request.
   * @param route Route from the [base] to which the request will be sent.
   * @param resumption Policy for defining whether the request should be resumed in case it is
   *   interrupted.
   */
  suspend fun get(
    authentication: Authentication,
    route: String,
    resumption: Resumption = Resumption.None
  ): HttpResponse {
    return request(
      authentication,
      Request.MethodName.GET,
      route,
      resumption,
      Parameterization.empty
    ) {
      client.get(route) { it() }
    }
  }

  /**
   * Sends an unparameterized HTTP `POST` request.
   *
   * @param authentication Authentication requirement that is appropriate for this specific request.
   * @param route Route from the [base] to which the request will be sent.
   * @param resumption Policy for defining whether the request should be resumed in case it is
   *   interrupted.
   */
  suspend fun post(
    authentication: Authentication,
    route: String,
    resumption: Resumption = Resumption.None
  ): HttpResponse {
    return post(authentication, route, Parameterization.empty, resumption) {
      client.post(route) { it() }
    }
  }

  /**
   * Sends an HTTP `POST` request.
   *
   * @param authentication Authentication requirement that is appropriate for this specific request.
   * @param route Route from the [base] to which the request will be sent.
   * @param parameters [Parameters] to be added to the form.
   * @param resumption Policy for defining whether the request should be resumed in case it is
   *   interrupted.
   */
  suspend fun post(
    authentication: Authentication,
    route: String,
    parameters: Parameters,
    resumption: Resumption = Resumption.None
  ): HttpResponse {
    return post(authentication, route, Parameterization.Body(parameters), resumption) {
      if (parameters.isEmpty()) {
        client.post(route) { it() }
      } else {
        coroutineScope { client.submitForm(route, parameters) { launch { it() } } }
      }
    }
  }

  /**
   * Sends an HTTP `POST` request.
   *
   * @param authentication Authentication requirement that is appropriate for this specific request.
   * @param route Route from the [base] to which the request will be sent.
   * @param form `multipart/form-data`-encoded parts to be added as headers.
   * @param resumption Policy for defining whether the request should be resumed in case it is
   *   interrupted.
   */
  suspend fun post(
    authentication: Authentication,
    route: String,
    form: List<PartData>,
    resumption: Resumption = Resumption.None
  ): HttpResponse {
    return post(authentication, route, Parameterization.Headers(form), resumption) {
      client.submitFormWithBinaryData(route, form) { it() }
    }
  }

  /** Interrupts all of the ongoing requests. */
  fun interrupt() {
    ongoing
      .onEach { (request, deferred) -> deferred.cancel(InterruptionException(request)) }
      .clear()
  }

  /** Configures retrying behavior on [HttpRequest]s that fail due to server errors. */
  private fun HttpClientConfig<*>.retryAfterFailures() {
    install(HttpRequestRetry) {
      retryOnServerErrors(2)
      exponentialDelay()
    }
  }

  /**
   * Prepares an HTTP `POST` request to be sent.
   *
   * @param authentication Authentication requirement that is appropriate for this specific request.
   * @param route Route from the [base] to which the request will be sent.
   * @param parameterization Content of the parameters which vary in type depending on where they
   *   are inserted in the request.
   * @param resumption Policy for defining whether the request should be resumed in case it is
   *   interrupted.
   * @param request Actual performance of the `POST` request.
   */
  private suspend inline fun post(
    authentication: Authentication,
    route: String,
    parameterization: Parameterization<*>,
    resumption: Resumption,
    crossinline request: suspend (config: suspend HttpRequestBuilder.() -> Unit) -> HttpResponse
  ): HttpResponse {
    return request(
      authentication,
      Request.MethodName.POST,
      route,
      resumption,
      parameterization,
      request
    )
  }

  /**
   * Persists the request intended to be performed in the lambda whose return is awaited in another
   * [CoroutineScope] within this [Requester]; then, removes the persisted information afterwards
   * when it responds. The produced response gets cached and maintained alive for a given amount of
   * time (specified by [timeToLive]), allowing for repeated calls to this method within that period
   * to reuse and return the cached response instead of actually executing the [request] again.
   *
   * In case this particular request was made resumable and it is interrupted, it will then be
   * executed again when [resume] is called.
   *
   * @param authentication Authentication requirement that is appropriate for this specific request.
   * @param methodName Name of the HTTP method that's equivalent to that of the request to be
   *   performed.
   * @param route Specific resource on which the HTTP method is being called.
   * @param resumption Policy for defining whether the request should be resumed in case it is
   *   interrupted.
   * @param parameterization Content of the parameters which vary in type depending on where they
   *   are inserted in the request.
   * @param request Actual performance of the request to which the HTTP method refers.
   * @see Resumption.Resumable
   */
  @OptIn(ExperimentalContracts::class)
  private suspend inline fun request(
    authentication: Authentication,
    @Request.MethodName methodName: String,
    route: String,
    resumption: Resumption,
    parameterization: Parameterization<*>,
    crossinline request: suspend (config: suspend HttpRequestBuilder.() -> Unit) -> HttpResponse
  ): HttpResponse {
    contract { callsInPlace(request, InvocationKind.AT_MOST_ONCE) }
    return coroutineScope {
      val serializedParameters = parameterization.serializedContent
      val requestEntity =
        Request(authentication, methodName, route, parameterization.name, serializedParameters)
      resumption.prepare(requestDao, requestEntity)
      val responseDeferred =
        async(start = CoroutineStart.LAZY) { retrieveOrRememberResponse(requestEntity, request) }
      ongoing[requestEntity] = responseDeferred
      val response = responseDeferred.await()
      ongoing -= requestEntity
      requestDao.delete(requestEntity)
      response
    }
  }

  /**
   * Obtains the [HttpResponse] that's been cached and previously associated to the given
   * [requestEntity] if it hasn't yet become stale or performs the [request] in case it has. When
   * the [request] lambda is invoked, its resulting [HttpResponse] is cached afterwards.
   *
   * @param requestEntity Request metadata based on which a cached [HttpResponse] may be retrieved.
   * @param request Actual performance of the request to be executed when an alive [HttpResponse]
   *   isn't available.
   */
  @OptIn(ExperimentalContracts::class)
  private suspend inline fun retrieveOrRememberResponse(
    requestEntity: Request,
    crossinline request: suspend (config: suspend HttpRequestBuilder.() -> Unit) -> HttpResponse
  ): HttpResponse {
    contract { callsInPlace(request, InvocationKind.AT_MOST_ONCE) }
    return cache[requestEntity]
      ?: request { requestEntity.authentication.lock(authenticationLock, this) }
        .also { cache(requestEntity, it) }
  }

  /**
   * Caches the [response] for a specific amount of time, defined by [timeToLive].
   *
   * @param requestEntity Metadata to which the [response] will be associated.
   * @param response [HttpResponse] to be cached.
   */
  private suspend fun cache(requestEntity: Request, response: HttpResponse) {
    scope().launch {
      cache[requestEntity] = response
      delay(timeToLive)
      cache.remove(requestEntity)
    }
  }

  companion object {
    /** [Duration] for which a request is considered to be alive and its response can be reused. */
    @InternalNetworkApi @VisibleForTesting val timeToLive = 5.seconds
  }
}

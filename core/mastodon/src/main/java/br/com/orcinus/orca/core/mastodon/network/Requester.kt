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
import br.com.orcinus.orca.core.mastodon.network.request.headers.strings.serializer
import br.com.orcinus.orca.core.mastodon.network.request.headers.strings.toParameters
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
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Parameters
import io.ktor.util.StringValues
import java.net.URI
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

/**
 * Manages and automatically retries in case of failures HTTP requests that have been requested to
 * be performed, caching their results for resource saving and effortless retrieval when an internet
 * connection isn't available.
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
  private val base: URI
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
          post(
            it.authentication,
            it.route,
            Json.decodeFromString(StringValues.serializer(), it.parameters).toParameters(),
            Resumption.Resumable
          )
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

  /** Interrupts all of the ongoing requests. */
  fun interrupt() {
    ongoing
      .onEach { (request, deferred) -> deferred.cancel(InterruptionException(request)) }
      .clear()
  }

  /**
   * Prepares an HTTP `POST` request to be sent.
   *
   * @param T Parameters held by [parameterization].
   * @param authentication Authentication requirement that is appropriate for this specific request.
   * @param route Route from the [base] to which the request will be sent.
   * @param parameterization Content of the parameters which vary in type depending on where they
   *   are inserted in the request.
   * @param resumption Policy for defining whether the request should be resumed in case it is
   *   interrupted.
   * @param request Actual performance of the `POST` request.
   */
  @InternalNetworkApi
  @VisibleForTesting
  internal suspend inline fun <reified T : Any> post(
    authentication: Authentication,
    route: String,
    parameterization: Parameterization<T>,
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
   * when it responds.
   *
   * In case cancellation occurs while there were ongoing requests, those that have been marked as
   * resumable will be executed again when [resume] is called.
   *
   * @param T Parameters held by [parameterization].
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
  private suspend inline fun <reified T : Any> request(
    authentication: Authentication,
    @Request.MethodName methodName: String,
    route: String,
    resumption: Resumption,
    parameterization: Parameterization<T>,
    crossinline request: suspend (config: suspend HttpRequestBuilder.() -> Unit) -> HttpResponse
  ): HttpResponse {
    contract { callsInPlace(request, InvocationKind.EXACTLY_ONCE) }
    return coroutineScope {
      val parametersInJson =
        Json.encodeToString(parameterization.serializer, parameterization.content)
      val requestEntity = Request(authentication, methodName, route, parametersInJson)
      resumption.prepare(requestDao, requestEntity)
      val responseDeferred =
        async(start = CoroutineStart.LAZY) {
          request { authentication.lock(authenticationLock, this) }
        }
      ongoing[requestEntity] = responseDeferred
      val response = responseDeferred.await()
      ongoing -= requestEntity
      requestDao.delete(requestEntity)
      response
    }
  }

  /** Configures retrying behavior on [HttpRequest]s that fail due to server errors. */
  private fun HttpClientConfig<*>.retryAfterFailures() {
    install(HttpRequestRetry) {
      retryOnServerErrors(2)
      exponentialDelay()
    }
  }
}

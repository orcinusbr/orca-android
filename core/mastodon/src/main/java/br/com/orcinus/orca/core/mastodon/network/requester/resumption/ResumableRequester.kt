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

package br.com.orcinus.orca.core.mastodon.network.requester.resumption

import android.content.Context
import androidx.annotation.VisibleForTesting
import br.com.orcinus.orca.core.mastodon.MastodonDatabase
import br.com.orcinus.orca.core.mastodon.network.InternalNetworkApi
import br.com.orcinus.orca.core.mastodon.network.requester.Logger
import br.com.orcinus.orca.core.mastodon.network.requester.Requester
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.Parameterization
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.Request
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.RequestDao
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.Resumption
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.injector.module.Module
import br.com.orcinus.orca.std.uri.url.HostedURLBuilder
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Parameters
import io.ktor.http.content.PartData
import io.ktor.util.StringValues
import java.net.URI
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Requester whose requests are resumable: whenever they're abruptly interrupted, they're
 * automatically retried after a call to [resume].
 *
 * @property elapsedTimeProvider [ResumableRequester.ElapsedTimeProvider] with which each request
 *   will be timestamped.
 * @property requestDao [RequestDao] for performing read and write operations on [Request]s.
 * @property logger [Logger] by which received [HttpResponse]s will be logged.
 * @property clientEngineFactory [HttpClientEngineFactory] that creates the [HttpClientEngine]
 *   powering the underlying [client].
 * @property baseURI [URI] from which routes are constructed.
 */
internal class ResumableRequester
@InternalNetworkApi
constructor(
  private val elapsedTimeProvider: ElapsedTimeProvider,
  private val requestDao: RequestDao,
  logger: Logger,
  baseURI: URI,
  clientEngineFactory: HttpClientEngineFactory<*>
) : Requester(logger, baseURI, clientEngineFactory) {
  /** Responses that are currently ongoing. */
  private val progress = hashMapOf<Request, Deferred<HttpResponse>>()

  /**
   * Responses to previously performed requests that haven't yet become stale (that is, are alive)
   * and are eligible for reuse.
   */
  private val reuse = hashMapOf<Request, HttpResponse>()

  /**
   * Provider of the current Unix timestamp.
   *
   * @see provide
   */
  @InternalNetworkApi
  internal fun interface ElapsedTimeProvider {
    /** Provides the elapsed time. */
    fun provide(): Duration

    companion object {
      /** [ElapsedTimeProvider] that provides the system's current time in [milliseconds]. */
      val system = ElapsedTimeProvider { System.currentTimeMillis().milliseconds }
    }
  }

  /**
   * [CancellationException] that is the cause of when a request is interrupted.
   *
   * @param request Metadata of the interrupted request.
   * @see interrupt
   */
  class InterruptionException @InternalNetworkApi internal constructor(request: Request) :
    CancellationException("$request has been interrupted by its requester.")

  override suspend fun delete(
    route: HostedURLBuilder.() -> HostedURLBuilder,
    build: HttpRequestBuilder.() -> Unit
  ): HttpResponse {
    return prepareForResumption(Request.MethodName.DELETE, Parameterization.empty, route) {
      super.delete(route, build)
    }
  }

  override suspend fun get(
    parameters: Parameters,
    route: HostedURLBuilder.() -> HostedURLBuilder,
    build: HttpRequestBuilder.() -> Unit
  ): HttpResponse {
    return prepareForResumption(Request.MethodName.GET, Parameterization.Body(parameters), route) {
      super.get(parameters, route, build)
    }
  }

  override suspend fun post(
    parameters: Parameters,
    route: HostedURLBuilder.() -> HostedURLBuilder,
    build: HttpRequestBuilder.() -> Unit
  ): HttpResponse {
    return prepareForResumption(Request.MethodName.POST, Parameterization.Body(parameters), route) {
      super.post(parameters, route, build)
    }
  }

  override suspend fun post(
    form: List<PartData>,
    route: HostedURLBuilder.() -> HostedURLBuilder,
    build: HttpRequestBuilder.() -> Unit
  ): HttpResponse {
    return prepareForResumption(Request.MethodName.POST, Parameterization.Headers(form), route) {
      super.post(form, route, build)
    }
  }

  /**
   * Restarts requests that have been attempted to be performed previously and were cancelled when
   * this [ResumableRequester] was last interrupted, whose information were persisted by the
   * HTTP-analogous method when it called [prepareForResumption].
   *
   * @see interrupt
   */
  suspend fun resume() {
    requestDao.selectAll().forEach {
      val route = { _: HostedURLBuilder -> HostedURLBuilder.from(URI(it.route)) }
      it.fold(
        onDelete = { delete(route) },
        onGet = {
          get(
            (Parameterization.deserialize(Parameterization.Body.name, it.parameters)
                as Parameterization.Body)
              .content,
            route
          )
        },
        onPost = {
          when (
            val parameterization = Parameterization.deserialize(it.parameterization, it.parameters)
          ) {
            is Parameterization.Body -> post(parameterization.content, route)
            is Parameterization.Headers -> post(parameterization.content, route)
          }
        }
      )
    }
  }

  /** Interrupts all of the ongoing requests. */
  fun interrupt() {
    progress
      .onEach { (request, deferred) -> deferred.cancel(InterruptionException(request)) }
      .clear()
  }

  /**
   * Persists the request intended to be performed in the lambda whose return is awaited in another
   * [CoroutineScope] within this [ResumableRequester]; then, removes the persisted information
   * afterwards when it responds. The produced response is made alive for a given amount of time
   * (specified by [timeToLive]), allowing for repeated calls to this method within that period to
   * reuse and return the "cached" response instead of actually executing the [request] again.
   *
   * In case the [request] gets interrupted, it will then be executed again when [resume] is called.
   *
   * @param methodName Name of the HTTP method that's equivalent to that of the request to be
   *   performed.
   * @param parameterization Content of the parameters which vary in type depending on where they
   *   are inserted in the request.
   * @param route Builds the route from the [baseURI] to which the request will be sent.
   * @param request Actual performance of the request to which the HTTP method refers.
   * @see Resumption.Resumable
   */
  @OptIn(ExperimentalContracts::class)
  private suspend inline fun prepareForResumption(
    @Request.MethodName methodName: String,
    parameterization: Parameterization<*>,
    noinline route: HostedURLBuilder.() -> HostedURLBuilder,
    crossinline request: suspend () -> HttpResponse
  ): HttpResponse {
    contract { callsInPlace(request, InvocationKind.AT_MOST_ONCE) }
    return coroutineScope {
      val entity =
        retrieveOrCreateRequest(
          methodName,
          absolute(route),
          parameterization.name,
          parameterization.serializedContent
        )
      requestDao.insert(entity)
      val response = progress.computeIfAbsent(entity) { async { respond(entity, request) } }.await()
      progress -= entity
      requestDao.delete(entity)
      response
    }
  }

  /**
   * Either retrieves a [Request] that has an ID that equals to that which is generated for the
   * given characteristics or creates a new one.
   *
   * @param methodName Name of the HTTP method called on the [route].
   * @param route Specific, absolute resource on which the HTTP method is being called.
   * @param parameterizationName Name of the strategy for parameterizing the request.
   * @param serializedParameters Serialized version of [StringValues] or multiple [PartData].
   */
  private suspend fun retrieveOrCreateRequest(
    @Request.MethodName methodName: String,
    route: String,
    parameterizationName: String,
    serializedParameters: String
  ): Request {
    val id = Request.generateID(methodName, route, parameterizationName, serializedParameters)
    return requestDao.selectByID(id)
      ?: Request(
        methodName,
        route,
        parameterizationName,
        serializedParameters,
        timestamp = elapsedTimeProvider.provide().inWholeMilliseconds
      )
  }

  /**
   * Obtains the [HttpResponse] that's been associated to the given [entity] previously if it hasn't
   * yet become stale or performs the [request] in case it has. When the [request] lambda is
   * invoked, its resulting [HttpResponse] is maintained alive afterwards for future reuse.
   *
   * @param entity Request metadata based on which a cached [HttpResponse] may be retrieved.
   * @param request Actual performance of the request to be executed when an alive [HttpResponse]
   *   isn't available.
   * @see timeToLive
   */
  @OptIn(ExperimentalContracts::class)
  private suspend inline fun respond(
    entity: Request,
    crossinline request: suspend () -> HttpResponse
  ): HttpResponse {
    contract { callsInPlace(request, InvocationKind.AT_MOST_ONCE) }
    val reusedResponse = reuse[entity]
    return if (reusedResponse == null || isResponseStale(entity)) {
      val fetchedResponse = request()
      reuse[entity] = fetchedResponse
      fetchedResponse
    } else {
      reusedResponse
    }
  }

  /**
   * Returns whether the response associated to the [request] is stale.
   *
   * @param request [Request] whose response's staleness will be checked.
   */
  private fun isResponseStale(request: Request): Boolean {
    return elapsedTimeProvider.provide() - request.timestamp.milliseconds > timeToLive
  }

  companion object {
    /** [Duration] for which a request is considered to be alive and its response can be reused. */
    @InternalNetworkApi @VisibleForTesting val timeToLive = 5.seconds
  }
}

/**
 * Creates a [ResumableRequester] based on the receiver [Requester].
 *
 * @throws Module.DependencyNotInjectedException If a [Context] hasn't been globally injected.
 */
@Throws(Module.DependencyNotInjectedException::class)
internal fun Requester.resumable(): ResumableRequester {
  return toResumableRequester {
    val context = Injector.get<Context>()
    val requestDao = MastodonDatabase.getInstance(context).requestDao
    ResumableRequester(
      ResumableRequester.ElapsedTimeProvider.system,
      requestDao,
      logger,
      baseURI,
      clientEngineFactory
    )
  }
}

/**
 * Creates a [ResumableRequester] based on the receiver [Requester].
 *
 * @property elapsedTimeProvider [ResumableRequester.ElapsedTimeProvider] with which each request
 *   will be timestamped.
 * @property requestDao [RequestDao] for performing read and write operations on [Request]s.
 */
@Throws(Module.DependencyNotInjectedException::class)
internal fun Requester.resumable(
  elapsedTimeProvider: ResumableRequester.ElapsedTimeProvider,
  requestDao: RequestDao
): ResumableRequester {
  return toResumableRequester {
    ResumableRequester(elapsedTimeProvider, requestDao, logger, baseURI, clientEngineFactory)
  }
}

/**
 * Converts this [Requester] into a [ResumableRequester].
 *
 * @param conversion Lambda that gets invoked in case the receiver [Requester] isn't a resumable
 *   one, which produces the [ResumableRequester] to be returned.
 * @return The [Requester] on which this method was called when it is a [ResumableRequester];
 *   otherwise, the result of invoking [conversion].
 */
@OptIn(ExperimentalContracts::class)
private inline fun Requester.toResumableRequester(
  conversion: () -> ResumableRequester
): ResumableRequester {
  contract { callsInPlace(conversion, InvocationKind.AT_MOST_ONCE) }
  return if (this is ResumableRequester) {
    this
  } else {
    conversion()
  }
}

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

package br.com.orcinus.orca.core.mastodon.network.requester

import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.SomeAuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.network.requester.request.Authentication
import br.com.orcinus.orca.core.mastodon.network.requester.request.Request
import br.com.orcinus.orca.core.mastodon.network.requester.request.RequestDao
import br.com.orcinus.orca.core.mastodon.network.requester.request.Resumption
import br.com.orcinus.orca.core.mastodon.network.requester.request.serializer
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Parameters
import java.net.URL
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
 * @param client [HttpClient] that will be responsible for sending HTTP requests.
 * @param authenticationLock [AuthenticationLock] for unlocking operations made by an
 *   unauthenticated [Actor].
 * @param requestDao [RequestDao] for performing read and write operations on [Request]s.
 */
internal class Requester(
  private val client: HttpClient,
  private val authenticationLock: SomeAuthenticationLock,
  private val requestDao: RequestDao
) {
  /** Responses that are currently being obtained. */
  private val ongoing = hashMapOf<Request, Deferred<HttpResponse>>()

  /**
   * [CancellationException] that is the cause of when a request is interrupted.
   *
   * @param request Metadata of the interrupted request.
   * @see Requester.interrupt
   */
  class InterruptionException @InternalRequesterApi internal constructor(request: Request) :
    CancellationException("$request has been interrupted by its requester.")

  /**
   * Sends an HTTP `GET` request.
   *
   * @param authentication Authentication requirement that is appropriate for this specific request.
   * @param route Route from the base [URL] to which the request will be sent.
   * @param resumption Policy for defining whether the request should be resumed in case it is
   *   interrupted.
   */
  suspend fun get(
    authentication: Authentication,
    route: String,
    resumption: Resumption = Resumption.None
  ): HttpResponse {
    return request(authentication, Request.MethodName.GET, route, resumption, Parameters.Empty) {
      client.get(route) { authentication.lock(authenticationLock, this) }
    }
  }

  /**
   * Sends an unparameterized HTTP `POST` request.
   *
   * @param authentication Authentication requirement that is appropriate for this specific request.
   * @param route Route from the base [URL] to which the request will be sent.
   * @param resumption Policy for defining whether the request should be resumed in case it is
   *   interrupted.
   */
  suspend fun post(
    authentication: Authentication,
    route: String,
    resumption: Resumption = Resumption.None
  ): HttpResponse {
    return post(authentication, route, Parameters.Empty, resumption)
  }

  /**
   * Sends an HTTP `POST` request.
   *
   * @param authentication Authentication requirement that is appropriate for this specific request.
   * @param route Route from the base [URL] to which the request will be sent.
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
    return request(authentication, Request.MethodName.POST, route, resumption, parameters) {
      if (parameters.isEmpty()) {
        client.post(route) { authentication.lock(authenticationLock, this) }
      } else {
        coroutineScope {
          client.submitForm(route, parameters) requestBuilder@{
            launch { authentication.lock(authenticationLock, this@requestBuilder) }
          }
        }
      }
    }
  }

  /**
   * Restarts requests that have been attempted to be performed previously and were interrupted when
   * this [Requester] was last cancelled, whose information were persisted by the HTTP-analogous
   * method when it called [request].
   */
  suspend fun resume() {
    requestDao.selectAll().forEach { request ->
      request.fold(
        onGet = { get(request.authentication, request.route, Resumption.Resumable) },
        onPost = {
          post(
            request.authentication,
            request.route,
            Json.decodeFromString(Parameters.serializer(), request.parameters),
            Resumption.Resumable
          )
        }
      )
    }
  }

  /** Interrupts all of the ongoing requests. */
  fun interrupt() {
    ongoing
      .onEach { (request, deferred) -> deferred.cancel(InterruptionException(request)) }
      .clear()
  }

  /**
   * Persists the request intended to be performed in the lambda whose return is awaited in another
   * [CoroutineScope] within this [Requester]; then, removes the persisted information afterwards
   * when it responds.
   *
   * In case cancellation occurs while there were ongoing requests, those that have been marked as
   * resumable will be executed again when [resume] is called.
   *
   * @param authentication Authentication requirement that is appropriate for this specific request.
   * @param methodName Name of the HTTP method that's equivalent to that of the request to be
   *   performed.
   * @param route Specific resource on which the HTTP method is being called.
   * @param resumption Policy for defining whether the request should be resumed in case it is
   *   interrupted.
   * @param request Actual performance of the request to which the HTTP method refers.
   * @see Resumption.Resumable
   */
  @OptIn(ExperimentalContracts::class)
  private suspend fun request(
    authentication: Authentication,
    @Request.MethodName methodName: String,
    route: String,
    resumption: Resumption,
    parameters: Parameters,
    request: suspend () -> HttpResponse
  ): HttpResponse {
    contract { callsInPlace(request, InvocationKind.EXACTLY_ONCE) }
    return coroutineScope {
      val parametersInJson = Json.encodeToString(Parameters.serializer(), parameters)
      val requestEntity = Request(authentication, methodName, route, parametersInJson)
      resumption.prepare(requestDao, requestEntity)
      val responseDeferred = async(start = CoroutineStart.LAZY) { request() }
      ongoing[requestEntity] = responseDeferred
      val response = responseDeferred.await()
      ongoing -= requestEntity
      requestDao.delete(requestEntity)
      response
    }
  }
}

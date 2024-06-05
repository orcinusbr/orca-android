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

import androidx.annotation.CallSuper
import androidx.annotation.VisibleForTesting
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.network.InternalNetworkApi
import br.com.orcinus.orca.core.mastodon.network.requester.authentication.AuthenticatedRequester
import br.com.orcinus.orca.core.mastodon.network.requester.authentication.authenticated
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.ResumableRequester
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.resumable
import br.com.orcinus.orca.std.uri.url.HostedURLBuilder
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpRequestRetry
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

/**
 * Performs various kinds of HTTP requests.
 *
 * Requests can also be made resumable through [resumable], which produces a [ResumableRequester]
 * which automatically retries in case they're abruptly interrupted and get resumed posteriorly.
 *
 * As for those that require an authenticated [Actor], [authenticated] can be called in order to
 * create an [AuthenticatedRequester] by which authentication will be required when performing any
 * request.
 *
 * @property logger [Logger] by which received [HttpResponse]s will be logged.
 * @property clientEngineFactory [HttpClientEngineFactory] that creates the [HttpClientEngine]
 *   powering the underlying [client].
 * @property baseURI [URI] from which routes are constructed.
 * @see delete
 * @see get
 * @see post
 */
internal open class Requester
@InternalNetworkApi
constructor(
  @get:InternalNetworkApi internal val logger: Logger,
  @get:InternalNetworkApi internal val baseURI: URI,
  @get:InternalNetworkApi internal val clientEngineFactory: HttpClientEngineFactory<*>
) {
  /** [HttpClient] that will be responsible for sending HTTP requests. */
  private val client =
    HttpClient(clientEngineFactory) {
      logger.start(this)
      normalizeJsonKeys()
      retryAfterFailures()
    }

  /**
   * Sends an HTTP `DELETE` request.
   *
   * @param route Builds the route from the [baseURI] to which the request will be sent.
   */
  suspend fun delete(route: HostedURLBuilder.() -> URI): HttpResponse {
    return delete(route, noOpRequestBuild)
  }

  /**
   * Sends an HTTP `GET` request.
   *
   * @param parameters [Parameters] to be added to the body of the request.
   * @param route Builds the route from the [baseURI] to which the request will be sent.
   */
  suspend fun get(
    parameters: Parameters = Parameters.Empty,
    route: HostedURLBuilder.() -> URI
  ): HttpResponse {
    return get(parameters, route, noOpRequestBuild)
  }

  /**
   * Sends an HTTP `POST` request.
   *
   * @param parameters [Parameters] to be added to the form.
   * @param route Builds the route from the [baseURI] to which the request will be sent.
   */
  suspend fun post(
    parameters: Parameters = Parameters.Empty,
    route: HostedURLBuilder.() -> URI
  ): HttpResponse {
    return post(parameters, route, noOpRequestBuild)
  }

  /**
   * Sends an HTTP `POST` request.
   *
   * @param form `multipart/form-data`-encoded parts to be added as headers.
   * @param route Builds the route from the [baseURI] to which the request will be sent.
   */
  suspend fun post(form: List<PartData>, route: HostedURLBuilder.() -> URI): HttpResponse {
    return post(form, route, noOpRequestBuild)
  }

  /**
   * Sends an HTTP `DELETE` request.
   *
   * @param route Builds the route from the [baseURI] to which the request will be sent.
   * @param build Additional configuration for the request being built.
   */
  @CallSuper
  @InternalNetworkApi
  internal open suspend fun delete(
    route: HostedURLBuilder.() -> URI,
    build: HttpRequestBuilder.() -> Unit
  ): HttpResponse {
    return client.delete(absolute(route), build)
  }

  /**
   * Sends an HTTP `GET` request.
   *
   * @param route Builds the route from the [baseURI] to which the request will be sent.
   * @param parameters [Parameters] to be added to the body of the request.
   * @param build Additional configuration for the request being built.
   */
  @CallSuper
  @InternalNetworkApi
  internal open suspend fun get(
    parameters: Parameters = Parameters.Empty,
    route: HostedURLBuilder.() -> URI,
    build: HttpRequestBuilder.() -> Unit
  ): HttpResponse {
    return client.get(absolute(route)) {
      url.parameters.appendAll(parameters)
      build.invoke(this)
    }
  }

  /**
   * Sends an HTTP `POST` request.
   *
   * @param parameters [Parameters] to be added to the form.
   * @param route Builds the route from the [baseURI] to which the request will be sent.
   * @param build Additional configuration for the request being built.
   */
  @CallSuper
  @InternalNetworkApi
  internal open suspend fun post(
    parameters: Parameters = Parameters.Empty,
    route: HostedURLBuilder.() -> URI,
    build: HttpRequestBuilder.() -> Unit
  ): HttpResponse {
    return if (parameters.isEmpty()) {
      client.post(absolute(route), build)
    } else {
      client.submitForm(absolute(route), parameters, block = build)
    }
  }

  /**
   * Sends an HTTP `POST` request.
   *
   * @param form `multipart/form-data`-encoded parts to be added as headers.
   * @param route Builds the route from the [baseURI] to which the request will be sent.
   * @param build Additional configuration for the request being built.
   */
  @CallSuper
  @InternalNetworkApi
  internal open suspend fun post(
    form: List<PartData>,
    route: HostedURLBuilder.() -> URI,
    build: HttpRequestBuilder.() -> Unit
  ): HttpResponse {
    return if (form.isEmpty()) {
      client.post(absolute(route), build)
    } else {
      client.submitFormWithBinaryData(absolute(route), form, build)
    }
  }

  /**
   * Builds a [URI] based on the given routing and converts it into its absolute [String].
   *
   * @param route Builds the route from the [baseURI] to which the request will be sent.
   */
  @InternalNetworkApi
  @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
  internal fun absolute(route: HostedURLBuilder.() -> URI): String {
    return HostedURLBuilder.from(baseURI).route().toString()
  }

  /** Configures retrying behavior on requests that fail due to server errors. */
  private fun HttpClientConfig<*>.retryAfterFailures() {
    install(HttpRequestRetry) {
      exponentialDelay()
      retryOnServerErrors(2)
    }
  }

  companion object {
    /** Lambda in which the request being built isn't modified. */
    private val noOpRequestBuild: HttpRequestBuilder.() -> Unit = {}
  }
}

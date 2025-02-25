/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.core.mastodon.instance.requester

import androidx.annotation.CallSuper
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.authenticated
import br.com.orcinus.orca.core.mastodon.instance.requester.resumption.ResumableRequester
import br.com.orcinus.orca.core.mastodon.instance.requester.resumption.resumable
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.Parameters
import io.ktor.http.content.PartData
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.StringValuesBuilder
import java.net.URI
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

/**
 * Performs various kinds of HTTP requests.
 *
 * Requests can also be made resumable through [resumable], which produces a [ResumableRequester]
 * which automatically retries in case they're abruptly interrupted and get resumed posteriorly.
 *
 * As for those that require an authenticated [Actor], [authenticated] can be called in order to
 * create one by which authentication will be required when performing any request.
 *
 * @property logger [Logger] by which received [HttpResponse]s will be logged.
 * @property clientEngineFactory [HttpClientEngineFactory] that creates the [HttpClientEngine]
 *   powering the underlying [client].
 * @property baseURI [URI] from which routes are constructed.
 * @see delete
 * @see get
 * @see post
 */
open class Requester
internal constructor(
  @get:InternalRequesterApi internal val logger: Logger,
  @get:InternalRequesterApi internal val baseURI: URI,
  @get:InternalRequesterApi internal val clientEngineFactory: HttpClientEngineFactory<*>
) {
  /** [HttpClient] that will be responsible for sending HTTP requests. */
  @InternalRequesterApi
  internal val client =
    HttpClient(clientEngineFactory) {
      logger.start(this)
      normalizeJsonKeys()
      retryAfterFailures()
    }

  /**
   * Modifications that have been applied to a request to be performed.
   *
   * @property headers [Headers] that have been appended.
   */
  internal open class Configuration
  @InternalRequesterApi
  constructor(@get:InternalRequesterApi val headers: Headers) {
    /**
     * Builds a [Configuration].
     *
     * @see build
     */
    open class Builder @InternalRequesterApi constructor() {
      /** [Headers] to be appended. */
      protected var headers = Headers.Empty
        private set

      /**
       * Appends headers to the request.
       *
       * @param build Builds the headers to be added.
       */
      fun headers(build: StringValuesBuilder.() -> Unit) {
        headers =
          Headers.build {
            appendAll(headers)
            build(this)
          }
      }

      /** Builds a [Configuration] with the applied modifications. */
      @InternalRequesterApi
      open fun build(): Configuration {
        return Configuration(headers)
      }
    }

    /**
     * [Configuration] specific to an `application/x-www-form-urlencoded`-MIME-typed request.
     *
     * @property headers [Headers] that have been appended.
     * @property parameters [Parameters] that have been appended to the body.
     */
    class UrlEncoded
    @InternalRequesterApi
    constructor(headers: Headers, @get:InternalRequesterApi val parameters: Parameters) :
      Configuration(headers) {
      /**
       * [Configuration.Builder] that builds a [Configuration] for an
       * `application/x-www-form-urlencoded`-MIME-typed request.
       */
      class Builder : Configuration.Builder() {
        /** [Parameters] to be appended to the body. */
        private var parameters = Parameters.Empty

        /**
         * Appends parameters to the body of the request.
         *
         * @param build Builds the parameters to be added.
         */
        inline fun parameters(build: StringValuesBuilder.() -> Unit) {
          parameters =
            Parameters.build {
              appendAll(parameters)
              build(this)
            }
        }

        override fun build(): UrlEncoded {
          return UrlEncoded(headers, parameters)
        }
      }

      override fun applyTo(requestBuilder: HttpRequestBuilder) {
        super.applyTo(requestBuilder)
        requestBuilder.setBody(FormDataContent(parameters))
      }

      companion object {
        /**
         * [Configuration] for an `application/x-www-form-urlencoded`-MIME-typed request on which
         * modifications aren't performed.
         */
        private val empty = UrlEncoded(Headers.Empty, Parameters.Empty)

        /**
         * Instantiates a [Builder] for building the returned
         * `application/x-www-form-urlencoded`-specific [Configuration].
         *
         * @param build Modifies the [Configuration] to be built.
         */
        @InternalRequesterApi
        fun build(build: Builder.() -> Unit): UrlEncoded {
          return if (build === noOpBuild) {
            empty
          } else {
            Builder().apply(build).build()
          }
        }
      }
    }

    /**
     * Applies this [Configuration] to the [requestBuilder].
     *
     * @param requestBuilder [HttpRequestBuilder] to which the modifications encompassed by this
     *   [Configuration] will be applied.
     */
    @CallSuper
    @InternalRequesterApi
    open fun applyTo(requestBuilder: HttpRequestBuilder) {
      requestBuilder.headers.appendAll(headers)
    }

    companion object {
      /** [Configuration] for a request on which modifications aren't performed. */
      private val empty = Configuration(Headers.Empty)

      /** Lambda in which a request isn't configured. */
      @InternalRequesterApi val noOpBuild: Builder.() -> Unit = {}

      /**
       * Instantiates a [Builder] for building the returned [Configuration].
       *
       * @param build Modifies the [Configuration] to be built.
       */
      @InternalRequesterApi
      fun build(build: Builder.() -> Unit): Configuration {
        return if (build === noOpBuild) {
          empty
        } else {
          Builder().apply(build).build()
        }
      }
    }
  }

  /**
   * Sends an HTTP `DELETE` request.
   *
   * @param route Builds the route from the [baseURI] to which the request will be sent.
   * @param config Additionally configures the request.
   */
  internal suspend fun delete(
    route: HostedURLBuilder.() -> URI,
    config: Configuration.Builder.() -> Unit = Configuration.noOpBuild
  ): HttpResponse {
    return delete(Configuration.build(config), absolute(route), noOpRequestBuild)
  }

  /**
   * Sends an HTTP `GET` request.
   *
   * @param route Builds the route from the [baseURI] to which the request will be sent.
   * @param config Additionally configures the request.
   */
  internal suspend fun get(
    route: HostedURLBuilder.() -> URI,
    config: Configuration.Builder.() -> Unit = Configuration.noOpBuild
  ): HttpResponse {
    return get(Configuration.build(config), absolute(route), noOpRequestBuild)
  }

  /**
   * Sends an HTTP `POST` request.
   *
   * @param route Builds the route from the [baseURI] to which the request will be sent.
   * @param config Additionally configures the request.
   */
  internal suspend fun post(
    route: HostedURLBuilder.() -> URI,
    config: Configuration.UrlEncoded.Builder.() -> Unit = Configuration.noOpBuild
  ): HttpResponse {
    return post(Configuration.UrlEncoded.build(config), absolute(route), noOpRequestBuild)
  }

  /**
   * Sends an HTTP `POST` request.
   *
   * @param route Builds the route from the [baseURI] to which the request will be sent.
   * @param form `multipart/form-data`-encoded parts to be added as headers.
   * @param config Additionally configures the request.
   */
  internal suspend fun post(
    route: HostedURLBuilder.() -> URI,
    form: List<PartData>,
    config: Configuration.Builder.() -> Unit = Configuration.noOpBuild
  ): HttpResponse {
    return post(Configuration.build(config), absolute(route), form, noOpRequestBuild)
  }

  /**
   * Sends an HTTP `DELETE` request.
   *
   * @param config [Configuration] containing metadata about the request.
   * @param route Route from the [baseURI] to which the request will be sent.
   * @param build Additional configuration for the request being built.
   */
  @CallSuper
  @InternalRequesterApi
  internal open suspend fun delete(
    config: Configuration,
    route: URI,
    build: HttpRequestBuilder.() -> Unit
  ): HttpResponse {
    return client.delete("$route") {
      config.applyTo(this)
      build(this)
    }
  }

  /**
   * Sends an HTTP `GET` request.
   *
   * @param config [Configuration] containing metadata about the request.
   * @param route Route from the [baseURI] to which the request will be sent.
   * @param build Additional configuration for the request being built.
   */
  @CallSuper
  @InternalRequesterApi
  internal open suspend fun get(
    config: Configuration,
    route: URI,
    build: HttpRequestBuilder.() -> Unit
  ): HttpResponse {
    return client.get("$route") {
      config.applyTo(this)
      build(this)
    }
  }

  /**
   * Sends an HTTP `POST` request.
   *
   * @param config [Configuration] containing metadata about the request.
   * @param route Route from the [baseURI] to which the request will be sent.
   * @param build Additional configuration for the request being built.
   */
  @CallSuper
  @InternalRequesterApi
  internal open suspend fun post(
    config: Configuration,
    route: URI,
    build: HttpRequestBuilder.() -> Unit
  ): HttpResponse {
    return client.post("$route") {
      config.applyTo(this)
      build(this)
    }
  }

  /**
   * Sends an HTTP `POST` request.
   *
   * @param config [Configuration] containing metadata about the request.
   * @param route Route from the [baseURI] to which the request will be sent.
   * @param form `multipart/form-data`-encoded parts to be added as headers.
   * @param build Additional configuration for the request being built.
   */
  @CallSuper
  @InternalRequesterApi
  internal open suspend fun post(
    config: Configuration,
    route: URI,
    form: List<PartData>,
    build: HttpRequestBuilder.() -> Unit
  ): HttpResponse {
    return if (form.isEmpty()) {
      client.post("$route") {
        config.applyTo(this)
        build(this)
      }
    } else {
      client.submitFormWithBinaryData("$route", form) {
        config.applyTo(this)
        build(this)
      }
    }
  }

  /**
   * Returns an absolute [URI] based on the given routing.
   *
   * @param route Builds the route from the [baseURI] to which the request will be sent.
   */
  internal fun absolute(route: HostedURLBuilder.() -> URI): URI {
    return HostedURLBuilder.from(baseURI).route()
  }

  /**
   * Configures [Json] serialization behavior on content negotiation, ignoring unknown keys and
   * mapping known ones' names to snake case.
   */
  @InternalRequesterApi
  private fun HttpClientConfig<*>.normalizeJsonKeys() {
    install(ContentNegotiation) {
      json(
        Json {
          ignoreUnknownKeys = true

          @OptIn(ExperimentalSerializationApi::class)
          namingStrategy = JsonNamingStrategy.SnakeCase
        }
      )
    }
  }

  /** Configures retrying behavior on requests that fail due to server errors. */
  private fun HttpClientConfig<*>.retryAfterFailures() {
    install(HttpRequestRetry) {
      exponentialDelay()
      retryOnServerErrors(2)
    }
  }

  companion object {
    /** Lambda in which a request being built isn't modified. */
    private val noOpRequestBuild: HttpRequestBuilder.() -> Unit = {}

    /**
     * Creates a [Requester].
     *
     * @param baseURI [URI] from which routes are constructed.
     */
    @JvmStatic
    fun create(baseURI: URI): Requester {
      return Requester(Logger.Android, baseURI, clientEngineFactory = CIO)
    }
  }
}

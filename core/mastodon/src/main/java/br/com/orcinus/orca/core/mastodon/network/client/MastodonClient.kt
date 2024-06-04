/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.network.client

import androidx.annotation.Discouraged
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.network.InternalNetworkApi
import br.com.orcinus.orca.core.mastodon.network.requester.Logger
import br.com.orcinus.orca.core.mastodon.network.requester.normalizeJsonKeys
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.instanceProvider
import br.com.orcinus.orca.std.injector.Injector
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder
import io.ktor.http.Parameters
import io.ktor.http.content.PartData

/**
 * [AuthenticationLock] for requesting an unlock in `authenticateAnd*` methods.
 *
 * @see HttpClient.authenticateAndGet
 * @see HttpClient.authenticateAndPost
 * @see HttpClient.authenticateAndSubmitForm
 * @see HttpClient.authenticateAndSubmitFormWithBinaryData
 */
@PublishedApi
internal val authenticationLock
  get() = Injector.from<CoreModule>().instanceProvider().provide().authenticationLock

/**
 * [HttpClient] through which [HttpRequest]s can be sent to the Mastodon API.
 *
 * @param config Additional configuration to be done on the [HttpClient].
 */
@Discouraged("Prefer referencing a `Requester`'s `HttpClient` instead.")
@Suppress("FunctionName")
internal inline fun MastodonClient(
  crossinline config: HttpClientConfig<CIOEngineConfig>.() -> Unit
): HttpClient {
  @Suppress("DiscouragedApi") return MastodonClient(CIO, Logger.Android, config)
}

/**
 * [HttpClient] through which [HttpRequest]s can be sent to the Mastodon API.
 *
 * @param T [HttpClientEngineConfig] through which the [HttpClientConfig] will be configured.
 * @param engineFactory [HttpClientEngineFactory] for creating the [HttpClientEngine] that powers
 *   the resulting [HttpClient].
 * @param logger [Logger] by which received [HttpResponse]s will be logged.
 * @param config Additional configuration to be done on the [HttpClient].
 */
@Discouraged("Prefer referencing a `Requester`'s `HttpClient` instead.")
@Suppress("FunctionName")
internal inline fun <T : HttpClientEngineConfig> MastodonClient(
  engineFactory: HttpClientEngineFactory<T>,
  logger: Logger,
  crossinline config: HttpClientConfig<T>.() -> Unit = {}
): HttpClient {
  return HttpClient(engineFactory) {
    logger.start(this)
    normalizeJsonKeys()
    config()
  }
}

/**
 * Performs a DELETE [HttpRequest] to the [route] that requires an
 * [authenticated][Actor.Authenticated] [Actor].
 *
 * @param route URI [String] to which the [HttpRequest] will be sent
 * @param build Additional configuration for the [HttpRequest] to be performed.
 */
@Discouraged("Prefer performing an authenticated `DELETE` HTTP request through a `Requester`.")
internal suspend inline fun HttpClient.authenticateAndDelete(
  route: String,
  crossinline build: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
  return delete(route) {
    authenticate()
    build.invoke(this)
  }
}

/**
 * Performs a GET [HttpRequest] to the [route] that requires an [authenticated][Actor.Authenticated]
 * [Actor].
 *
 * @param route URI [String] to which the [HttpRequest] will be sent.
 * @param build Additional configuration for the [HttpRequest] to be performed.
 */
@Discouraged("Prefer performing an authenticated `GET` HTTP request through a `Requester`.")
internal suspend inline fun HttpClient.authenticateAndGet(
  route: String,
  crossinline build: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
  return get(route) {
    authenticate()
    build.invoke(this)
  }
}

/**
 * Performs a POST [HttpRequest] to the [route] that requires an
 * [authenticated][Actor.Authenticated] [Actor].
 *
 * @param route URI [String] to which the [HttpRequest] will be sent.
 * @param build Additional configuration for the [HttpRequest] to be performed.
 */
@Discouraged("Prefer performing an authenticated `POST` HTTP request through a `Requester`.")
internal suspend inline fun HttpClient.authenticateAndPost(
  route: String,
  crossinline build: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
  return post(route) {
    authenticate()
    build.invoke(this)
  }
}

/**
 * Performs a POST [HttpRequest] with the [parameters] included in the form to the [route] that
 * requires an [authenticated][Actor.Authenticated] [Actor].
 *
 * @param route URI [String] to which the [HttpRequest] will be sent.
 * @param parameters [Parameters] to be added to the form.
 * @param build Additional configuration for the [HttpRequest] to be performed.
 */
@Discouraged(
  "Prefer performing an authenticated, parameterized `POST` HTTP request through a `Requester`."
)
internal suspend inline fun HttpClient.authenticateAndSubmitForm(
  route: String,
  parameters: Parameters,
  crossinline build: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
  return authenticationLock.scheduleUnlock {
    submitForm(route, parameters) {
      bearerAuth(it.accessToken)
      build.invoke(this)
    }
  }
}

/**
 * Performs a POST [HttpRequest] to the [route] that requires an
 * [authenticated][Actor.Authenticated] [Actor], encoding the provided [formData] with the
 * `multipart/form-data` format.
 *
 * @param route URI [String] to which the [HttpRequest] will be sent.
 * @param formData [List] with [PartData] to be included in the form.
 * @param build Additional configuration for the [HttpRequest] to be performed.
 */
@Discouraged(
  "Prefer performing an authenticated, `multipart/form-data` `POST` HTTP request through a " +
    "`Requester`."
)
internal suspend inline fun HttpClient.authenticateAndSubmitFormWithBinaryData(
  route: String,
  formData: List<PartData>,
  crossinline build: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
  return submitFormWithBinaryData(route, formData) {
    authenticate()
    build.invoke(this)
  }
}

/**
 * Provides the [authenticated][Actor.Authenticated] [Actor]'s access token to the
 * [Authorization][HttpHeaders.Authorization] header through the [authenticationLock].
 */
@InternalNetworkApi
@PublishedApi
internal suspend fun HttpMessageBuilder.authenticate() {
  authenticationLock.scheduleUnlock { bearerAuth(it.accessToken) }
}

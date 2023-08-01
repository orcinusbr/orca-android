package com.jeanbarrossilva.orca.core.mastodon.client

import android.util.Log
import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.mastodon.BuildConfig
import com.jeanbarrossilva.orca.core.mastodon.Mastodon
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder
import io.ktor.http.Parameters
import io.ktor.http.content.PartData
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import org.koin.java.KoinJavaComponent

/** Tag with which [HttpResponse]s sent to the [MastodonHttpClient] will be logged. **/
private const val TAG = "MastodonHttpClient"

/**
 * [AuthenticationLock] through which an [authenticated][Actor.Authenticated] [Actor] will be
 * required.
 *
 * @see authenticate
 **/
private val authenticationLock
    get() = KoinJavaComponent.get<AuthenticationLock>(AuthenticationLock::class.java)

/** [HttpClient] through which [HttpRequest]s will be performed. **/
val MastodonHttpClient = HttpClient(CIO) {
    developmentMode = BuildConfig.DEBUG
    setUpDefaultRequest()
    setUpResponseLogging()
    setUpContentNegotiation()
}

/**
 * Performs a GET [HttpRequest] to the [route] that requires an [authenticated][Actor.Authenticated]
 * [Actor].
 *
 * @param route URL [String] to which the [HttpRequest] will be sent.
 * @param build Additional configuration for the [HttpResponse] to be performed.
 **/
internal suspend inline fun HttpClient.authenticateAndGet(
    route: String,
    crossinline build: HttpRequestBuilder.() -> Unit = { }
): HttpResponse {
    return get(route) {
        authenticate()
        build.invoke(this)
    }
}

/**
 * Performs a POST [HttpRequest] to the [route] that requires an [authenticated][Actor.Authenticated]
 * [Actor].
 *
 * @param route URL [String] to which the [HttpRequest] will be sent.
 * @param build Additional configuration for the [HttpResponse] to be performed.
 **/
internal suspend inline fun HttpClient.authenticateAndPost(
    route: String,
    build: HttpRequestBuilder.() -> Unit = { }
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
 * @param route URL [String] to which the [HttpRequest] will be sent.
 * @param parameters [Parameters] to be added to the form.
 * @param build Additional configuration for the [HttpResponse] to be performed.
 **/
internal suspend inline fun HttpClient.authenticateAndSubmitForm(
    route: String,
    parameters: Parameters,
    crossinline build: HttpRequestBuilder.() -> Unit = { }
) {
    authenticationLock.requestUnlock {
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
 * @param route URL [String] to which the [HttpRequest] will be sent.
 * @param formData [List] with [PartData] to be included in the form.
 * @param build Additional configuration for the [HttpResponse] to be performed.
 **/
internal suspend inline fun HttpClient.authenticateAndSubmitFormWithBinaryData(
    route: String,
    formData: List<PartData>,
    build: HttpRequestBuilder.() -> Unit = { }
): HttpResponse {
    return submitFormWithBinaryData(route, formData) {
        authenticate()
        build.invoke(this)
    }
}

/**
 * Applies default configurations to [HttpRequest]s sent to the [HttpClient] to be built to
 * avoid repetition or prevent mistakes from being made when performing them.
 **/
private fun HttpClientConfig<*>.setUpDefaultRequest() {
    defaultRequest {
        url("${Mastodon.baseUrl}")
    }
}

/** Observes [HttpResponse]s sent to the [HttpClient] to be built and logs them. **/
private fun HttpClientConfig<*>.setUpResponseLogging() {
    ResponseObserver {
        if (it.status.isSuccess()) {
            Log.i(TAG, it.format())
        } else {
            Log.e(TAG, it.format())
        }
    }
}

/** Formats this [HttpResponse] so that it is displayable. **/
private suspend fun HttpResponse.format(): String {
    val requestContent = request.content
    val requestFormDataParamsAsString =
        if (requestContent is FormDataContent) " (${requestContent.formData})" else ""
    return "${status.value} on ${request.method.value} ${request.url}" +
        "$requestFormDataParamsAsString:\n${bodyAsText()}"
}

/** Configures the behavior [ContentNegotiation]-related operations. **/
private fun HttpClientConfig<*>.setUpContentNegotiation() {
    install(ContentNegotiation) {
        setUpJsonSerialization()
    }
}

/** Configures [Json] serialization behavior. **/
private fun ContentNegotiation.Config.setUpJsonSerialization() {
    json(
        Json {
            ignoreUnknownKeys = true

            @OptIn(ExperimentalSerializationApi::class)
            namingStrategy = JsonNamingStrategy.SnakeCase
        }
    )
}

/**
 * Provides the [authenticated][Actor.Authenticated] [Actor]'s access token to the
 * [Authorization][HttpHeaders.Authorization] header through the [authenticationLock].
 **/
private suspend fun HttpMessageBuilder.authenticate() {
    authenticationLock.requestUnlock {
        bearerAuth(it.accessToken)
    }
}

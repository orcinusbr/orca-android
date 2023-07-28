package com.jeanbarrossilva.mastodonte.core.mastodon.client

import android.util.Log
import com.jeanbarrossilva.mastodonte.core.mastodon.BuildConfig
import com.jeanbarrossilva.mastodonte.core.mastodon.Mastodon
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

/** Tag with which [HttpResponse]s sent to the [MastodonHttpClient] will be logged. **/
private const val HTTP_CLIENT_TAG = "MastodonHttpClient"

/** [HttpClient] through which [HttpRequest]s will be performed. **/
val MastodonHttpClient = HttpClient(CIO) {
    developmentMode = BuildConfig.DEBUG
    setUpDefaultRequest()
    setUpResponseLogging()
    setUpContentNegotiation()
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
            Log.i(HTTP_CLIENT_TAG, it.format())
        } else {
            Log.e(HTTP_CLIENT_TAG, it.format())
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

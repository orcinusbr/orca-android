package com.jeanbarrossilva.mastodonte.core.mastodon

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.Url
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

internal object Mastodon {
    private const val HTTP_CLIENT_TAG = "Mastodon.httpClient"

    @Suppress("SpellCheckingInspection")
    const val CLIENT_ID = "F2Rx9d7C3x45KRVJ9rU4IjIJgrsjzaq74bSLo__VUG0"

    const val CLIENT_SECRET = BuildConfig.mastodonclientsecret

    val baseUrl = Url("https://mastodon.social")

    val httpClient = HttpClient(CIO) {
        setUpDefaultRequest()
        setUpResponseLogging()
        setUpContentNegotiation()
    }

    enum class ScopeProvisionSite {
        QUERY_PARAMETER {
            override val separator = '+'
        },
        FORM {
            override val separator = ' '
        };

        abstract val separator: Char
    }

    fun getScopes(provisionSite: ScopeProvisionSite): String {
        return arrayOf("read", "write", "follow").joinToString("${provisionSite.separator}")
    }

    private fun HttpClientConfig<*>.setUpDefaultRequest() {
        defaultRequest {
            url("$baseUrl")
        }
    }

    private fun HttpClientConfig<*>.setUpResponseLogging() {
        ResponseObserver {
            if (it.status.isSuccess()) {
                Log.i(HTTP_CLIENT_TAG, it.format())
            } else {
                Log.e(HTTP_CLIENT_TAG, it.format())
            }
        }
    }

    private suspend fun HttpResponse.format(): String {
        val requestContent = request.content
        val requestFormDataParamsAsString =
            if (requestContent is FormDataContent) "(${requestContent.formData})" else ""
        return "${status.value} on ${request.method.value} ${request.url} " +
            "$requestFormDataParamsAsString:\n${bodyAsText()}"
    }

    private fun HttpClientConfig<*>.setUpContentNegotiation() {
        install(ContentNegotiation) {
            setUpJsonSerialization()
        }
    }

    private fun ContentNegotiation.Config.setUpJsonSerialization() {
        json(
            Json {
                ignoreUnknownKeys = true

                @OptIn(ExperimentalSerializationApi::class)
                namingStrategy = JsonNamingStrategy.SnakeCase
            }
        )
    }
}

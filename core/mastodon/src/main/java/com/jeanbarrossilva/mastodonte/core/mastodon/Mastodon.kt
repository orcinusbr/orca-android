package com.jeanbarrossilva.mastodonte.core.mastodon

import android.util.Log
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
import io.ktor.http.Url
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

/** Mastodon-API-related keys and configurations. **/
internal object Mastodon {
    /** Tag with which [HttpResponse]s sent to the [httpClient] will be logged. **/
    private const val HTTP_CLIENT_TAG = "Mastodon.httpClient"

    /** Client ID (or key) that, unlinke the [clientSecret], is be publicly visible. **/
    @Suppress("SpellCheckingInspection")
    const val CLIENT_ID = "F2Rx9d7C3x45KRVJ9rU4IjIJgrsjzaq74bSLo__VUG0"

    /**
     * Client secret that, unlike the [CLIENT_ID], is private and is mainly used to obtain an access
     * token.
     **/
    @Suppress("MayBeConstant", "RedundantSuppression")
    val clientSecret = BuildConfig.mastodonclientSecret

    /** [Url] to which [HttpRequest] routes will be concatenated. **/
    val baseUrl = Url("https://mastodon.social")

    /** [HttpClient] through which [HttpRequest]s will be performed. **/
    val httpClient = HttpClient(CIO) {
        setUpDefaultRequest()
        setUpResponseLogging()
        setUpContentNegotiation()
    }

    /** Denotes the site through which scopes are being provided. **/
    enum class ScopeProvisionSite {
        /** Denotes that scopes are being provided through query parameters. **/
        QUERY_PARAMETER {
            override val separator = '+'
        },

        /** Denotes that scopes are being provided through a form. **/
        FORM {
            override val separator = ' '
        };

        /** [Char] used to separate different scopes. **/
        abstract val separator: Char
    }

    /**
     * Gets the scopes required by the application for it to function properly.
     *
     * @param provisionSite [ScopeProvisionSite] whose [separator][ScopeProvisionSite.separator]
     * will be used to format the resulting [String].
     * @return [String] formatted accordingly to the [provisionSite], containing all the required
     * scopes.
     **/
    fun getScopes(provisionSite: ScopeProvisionSite): String {
        return arrayOf("read", "write", "follow").joinToString("${provisionSite.separator}")
    }

    /**
     * Applies default configurations to [HttpRequest]s sent to the [HttpClient] to be built to
     * avoid repetition or prevent mistakes from being made when performing them.
     **/
    private fun HttpClientConfig<*>.setUpDefaultRequest() {
        defaultRequest {
            url("$baseUrl")
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
}

package com.jeanbarrossilva.orca.core.mastodon

import com.jeanbarrossilva.orca.core.http.client.CoreHttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequest

/** [CoreHttpClient] through which [HttpRequest]s will be performed. **/
val MastodonHttpClient = CoreHttpClient {
    defaultRequest {
        url("${Mastodon.baseUrl}")
    }
}

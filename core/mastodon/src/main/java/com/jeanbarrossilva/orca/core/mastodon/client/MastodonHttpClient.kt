package com.jeanbarrossilva.orca.core.mastodon.client

import com.jeanbarrossilva.orca.core.http.CoreHttpClient
import com.jeanbarrossilva.orca.core.mastodon.Mastodon
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequest

/** [CoreHttpClient] through which [HttpRequest]s will be performed. **/
val MastodonHttpClient = CoreHttpClient {
    defaultRequest {
        url("${Mastodon.baseUrl}")
    }
}

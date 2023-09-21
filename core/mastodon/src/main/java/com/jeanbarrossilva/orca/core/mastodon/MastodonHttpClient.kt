package com.jeanbarrossilva.orca.core.mastodon

import com.jeanbarrossilva.orca.core.http.client.CoreHttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequest
import io.ktor.http.Url
import io.ktor.http.takeFrom

/** Base [Url] of [MastodonHttpClient]. **/
val mastodonBaseURL = Url("https://mastodon.social")

/** [CoreHttpClient] through which [HttpRequest]s will be performed. **/
val MastodonHttpClient = CoreHttpClient {
    defaultRequest {
        url.takeFrom(mastodonBaseURL)
    }
}

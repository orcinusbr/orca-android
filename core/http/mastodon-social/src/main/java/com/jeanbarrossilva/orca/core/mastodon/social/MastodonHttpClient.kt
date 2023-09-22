package com.jeanbarrossilva.orca.core.mastodon.social

import com.jeanbarrossilva.orca.core.http.client.CoreHttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequest
import io.ktor.http.Url
import io.ktor.http.takeFrom

/** Base [Url] of [MastodonSocialHttpClient]. **/
val mastodonSocialBaseURL = Url("https://mastodon.social")

/** [CoreHttpClient] through which [HttpRequest]s will be sent to the mastodon.social instance. **/
val MastodonSocialHttpClient = CoreHttpClient {
    defaultRequest {
        url.takeFrom(mastodonSocialBaseURL)
    }
}

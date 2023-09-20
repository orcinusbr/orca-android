package com.jeanbarrossilva.orca.core.mastodon.client

import com.jeanbarrossilva.core.http.android.AndroidLogger
import com.jeanbarrossilva.orca.core.http.CoreHttpClient
import com.jeanbarrossilva.orca.core.mastodon.Mastodon
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequest

/** [HttpClient] through which [HttpRequest]s will be performed. **/
val MastodonHttpClient = CoreHttpClient(AndroidLogger.taggedAs("MastodonHttpClient")) {
    defaultRequest {
        url("${Mastodon.baseUrl}")
    }
}

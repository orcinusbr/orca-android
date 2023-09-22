package com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache

import com.jeanbarrossilva.orca.core.http.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpToot
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.status.HttpStatus
import com.jeanbarrossilva.orca.core.http.get
import com.jeanbarrossilva.orca.platform.cache.Fetcher
import io.ktor.client.HttpClient
import io.ktor.client.call.body

/** [Fetcher] that requests [HttpToot]s to the API. **/
object HttpTootFetcher : Fetcher<HttpToot>() {
    override suspend fun onFetch(key: String): HttpToot {
        return get<HttpClient>()
            .authenticateAndGet(authenticationLock = get(), "/api/v1/statuses/$key")
            .body<HttpStatus>()
            .toToot()
    }
}

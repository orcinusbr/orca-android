package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache

import com.jeanbarrossilva.orca.core.http.authenticateAndGet
import com.jeanbarrossilva.orca.core.mastodon.client.MastodonHttpClient
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.MastodonToot
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.status.Status
import com.jeanbarrossilva.orca.core.mastodon.get
import com.jeanbarrossilva.orca.platform.cache.Fetcher
import io.ktor.client.call.body

object MastodonTootFetcher : Fetcher<MastodonToot>() {
    override suspend fun onFetch(key: String): MastodonToot {
        return MastodonHttpClient
            .authenticateAndGet(authenticationLock = get(), "/api/v1/statuses/$key")
            .body<Status>()
            .toToot()
    }
}

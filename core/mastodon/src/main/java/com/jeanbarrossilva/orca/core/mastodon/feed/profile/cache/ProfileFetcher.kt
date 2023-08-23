package com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache

import com.jeanbarrossilva.orca.cache.Fetcher
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.mastodon.client.MastodonHttpClient
import com.jeanbarrossilva.orca.core.mastodon.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.account.MastodonAccount
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.status.TootPaginateSource
import io.ktor.client.call.body

class ProfileFetcher(private val tootPaginateSource: TootPaginateSource) :
    Fetcher<String, Profile>() {
    override suspend fun onFetch(key: String): Profile {
        return MastodonHttpClient
            .authenticateAndGet("/api/v1/accounts/$key")
            .body<MastodonAccount>()
            .toProfile(tootPaginateSource)
    }
}

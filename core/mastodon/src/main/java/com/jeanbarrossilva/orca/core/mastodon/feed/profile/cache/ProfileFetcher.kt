package com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.mastodon.client.MastodonHttpClient
import com.jeanbarrossilva.orca.core.mastodon.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.ProfileTootPaginateSource
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.account.MastodonAccount
import com.jeanbarrossilva.orca.platform.cache.Fetcher
import io.ktor.client.call.body

class ProfileFetcher(private val tootPaginateSourceProvider: ProfileTootPaginateSource.Provider) :
    Fetcher<String, Profile>() {
    override suspend fun onFetch(key: String): Profile {
        return MastodonHttpClient
            .authenticateAndGet("/api/v1/accounts/$key")
            .body<MastodonAccount>()
            .toProfile(tootPaginateSourceProvider)
    }
}

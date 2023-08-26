package com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache

import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.feed.profile.search.toProfileSearchResult
import com.jeanbarrossilva.orca.core.mastodon.client.MastodonHttpClient
import com.jeanbarrossilva.orca.core.mastodon.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.ProfileTootPaginateSource
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.account.MastodonAccount
import com.jeanbarrossilva.orca.platform.cache.Fetcher
import io.ktor.client.call.body
import io.ktor.client.request.parameter

class ProfileSearchResultsFetcher(
    private val tootPaginateSourceProvider: ProfileTootPaginateSource.Provider
) : Fetcher<List<ProfileSearchResult>>() {
    override suspend fun onFetch(key: String): List<ProfileSearchResult> {
        return MastodonHttpClient
            .authenticateAndGet("/api/v1/accounts/search") { parameter("q", key) }
            .body<List<MastodonAccount>>()
            .map { it.toProfile(tootPaginateSourceProvider).toProfileSearchResult() }
    }
}

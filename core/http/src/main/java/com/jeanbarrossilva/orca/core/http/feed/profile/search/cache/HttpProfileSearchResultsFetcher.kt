package com.jeanbarrossilva.orca.core.http.feed.profile.search.cache

import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.feed.profile.search.toProfileSearchResult
import com.jeanbarrossilva.orca.core.http.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.http.feed.profile.ProfileTootPaginateSource
import com.jeanbarrossilva.orca.core.http.feed.profile.account.HttpAccount
import com.jeanbarrossilva.orca.core.http.get
import com.jeanbarrossilva.orca.platform.cache.Fetcher
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter

class HttpProfileSearchResultsFetcher(
    private val tootPaginateSourceProvider: ProfileTootPaginateSource.Provider
) : Fetcher<List<ProfileSearchResult>>() {
    override suspend fun onFetch(key: String): List<ProfileSearchResult> {
        return get<HttpClient>()
            .authenticateAndGet(authenticationLock = get(), "/api/v1/accounts/search") {
                parameter("q", key)
            }
            .body<List<HttpAccount>>()
            .map { it.toProfile(tootPaginateSourceProvider).toProfileSearchResult() }
    }
}

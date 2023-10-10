package com.jeanbarrossilva.orca.core.http.feed.profile.search.cache

import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.feed.profile.search.toProfileSearchResult
import com.jeanbarrossilva.orca.core.http.HttpModule
import com.jeanbarrossilva.orca.core.http.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.http.feed.profile.ProfileTootPaginateSource
import com.jeanbarrossilva.orca.core.http.feed.profile.account.HttpAccount
import com.jeanbarrossilva.orca.core.http.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.core.http.instanceProvider
import com.jeanbarrossilva.orca.platform.cache.Fetcher
import com.jeanbarrossilva.orca.std.injector.Injector
import io.ktor.client.call.body
import io.ktor.client.request.parameter

internal class HttpProfileSearchResultsFetcher(
    private val tootPaginateSourceProvider: ProfileTootPaginateSource.Provider
) : Fetcher<List<ProfileSearchResult>>() {
    override suspend fun onFetch(key: String): List<ProfileSearchResult> {
        return (Injector.from<HttpModule>().instanceProvider.provide() as SomeHttpInstance)
            .client
            .authenticateAndGet("/api/v1/accounts/search") { parameter("q", key) }
            .body<List<HttpAccount>>()
            .map { it.toProfile(tootPaginateSourceProvider).toProfileSearchResult() }
    }
}

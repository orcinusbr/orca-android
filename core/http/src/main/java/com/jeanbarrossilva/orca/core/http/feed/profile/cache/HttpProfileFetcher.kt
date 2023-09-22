package com.jeanbarrossilva.orca.core.http.feed.profile.cache

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.http.HttpBridge
import com.jeanbarrossilva.orca.core.http.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.http.feed.profile.ProfileTootPaginateSource
import com.jeanbarrossilva.orca.core.http.feed.profile.account.HttpAccount
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpToot
import com.jeanbarrossilva.orca.platform.cache.Fetcher
import io.ktor.client.call.body

/**
 * [Fetcher] for [Profile]s.
 *
 * @param tootPaginateSourceProvider [ProfileTootPaginateSource.Provider] by which a
 * [ProfileTootPaginateSource] for paginating through a [HttpProfile]'s [HttpToot]s will be
 * provided.
 **/
internal class HttpProfileFetcher(
    private val tootPaginateSourceProvider: ProfileTootPaginateSource.Provider
) : Fetcher<Profile>() {
    override suspend fun onFetch(key: String): Profile {
        return HttpBridge
            .instance
            .client
            .authenticateAndGet("/api/v1/accounts/$key")
            .body<HttpAccount>()
            .toProfile(tootPaginateSourceProvider)
    }
}

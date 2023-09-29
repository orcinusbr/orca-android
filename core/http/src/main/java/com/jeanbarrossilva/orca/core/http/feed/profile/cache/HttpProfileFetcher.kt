package com.jeanbarrossilva.orca.core.http.feed.profile.cache

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.http.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.http.feed.profile.HttpProfile
import com.jeanbarrossilva.orca.core.http.feed.profile.ProfileTootPaginateSource
import com.jeanbarrossilva.orca.core.http.feed.profile.account.HttpAccount
import com.jeanbarrossilva.orca.core.http.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.platform.cache.Fetcher
import com.jeanbarrossilva.orca.std.injector.Injector
import io.ktor.client.call.body

/**
 * [Fetcher] for [HttpProfile]s.
 *
 * @param tootPaginateSourceProvider [ProfileTootPaginateSource.Provider] by which a
 * [ProfileTootPaginateSource] for paginating through an [HttpProfile]'s [Toot]s will be provided.
 **/
internal class HttpProfileFetcher(
    private val tootPaginateSourceProvider: ProfileTootPaginateSource.Provider
) : Fetcher<Profile>() {
    override suspend fun onFetch(key: String): Profile {
        return Injector
            .get<SomeHttpInstance>()
            .client
            .authenticateAndGet("/api/v1/accounts/$key")
            .body<HttpAccount>()
            .toProfile(tootPaginateSourceProvider)
    }
}

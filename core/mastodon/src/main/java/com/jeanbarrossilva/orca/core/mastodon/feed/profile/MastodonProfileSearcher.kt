package com.jeanbarrossilva.orca.core.mastodon.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.ProfileSearcher
import com.jeanbarrossilva.orca.core.mastodon.client.MastodonHttpClient
import com.jeanbarrossilva.orca.core.mastodon.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.account.MastodonAccount
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.status.TootPaginateSource
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MastodonProfileSearcher(private val tootPaginateSource: TootPaginateSource) :
    ProfileSearcher() {
    override suspend fun onSearch(query: String): Flow<List<Profile>> {
        return flow {
            MastodonHttpClient
                .authenticateAndGet("/api/v1/accounts/search") { parameter("q", query) }
                .body<List<MastodonAccount>>()
                .map { it.toProfile(tootPaginateSource) }
                .also { emit(it) }
        }
    }
}

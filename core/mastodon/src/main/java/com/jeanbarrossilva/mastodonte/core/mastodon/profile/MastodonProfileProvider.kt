package com.jeanbarrossilva.mastodonte.core.mastodon.profile

import com.jeanbarrossilva.mastodonte.core.mastodon.account.MastodonAccount
import com.jeanbarrossilva.mastodonte.core.mastodon.client.MastodonHttpClient
import com.jeanbarrossilva.mastodonte.core.mastodon.client.authenticateAndGet
import com.jeanbarrossilva.mastodonte.core.mastodon.toot.status.TootPaginateSource
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.ProfileProvider
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MastodonProfileProvider(private val tootPaginateSource: TootPaginateSource) :
    ProfileProvider() {
    override suspend fun contains(id: String): Boolean {
        return MastodonHttpClient.authenticateAndGet("/api/v1/accounts/$id").status.isSuccess()
    }

    override suspend fun onProvide(id: String): Flow<Profile> {
        return flow {
            MastodonHttpClient
                .authenticateAndGet("/api/v1/accounts/$id")
                .body<MastodonAccount>()
                .toProfile(tootPaginateSource)
                .let { emit(it) }
        }
    }
}

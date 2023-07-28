package com.jeanbarrossilva.mastodonte.core.mastodon.profile

import com.jeanbarrossilva.mastodonte.core.auth.AuthenticationLock
import com.jeanbarrossilva.mastodonte.core.mastodon.account.MastodonAccount
import com.jeanbarrossilva.mastodonte.core.mastodon.client.MastodonHttpClient
import com.jeanbarrossilva.mastodonte.core.mastodon.toot.status.TootPaginateSource
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.ProfileProvider
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MastodonProfileProvider(
    private val authenticationLock: AuthenticationLock,
    private val tootPaginateSource: TootPaginateSource
) : ProfileProvider() {
    private val pool = hashMapOf<String, Profile>()

    override suspend fun contains(id: String): Boolean {
        return MastodonHttpClient
            .get("/api/v1/accounts/$id") {
                authenticationLock.unlock {
                    bearerAuth(it.accessToken)
                }
            }
            .status
            .isSuccess()
    }

    override suspend fun onProvide(id: String): Flow<Profile> {
        return flow {
            MastodonHttpClient
                .get("/api/v1/accounts/$id") {
                    authenticationLock.unlock {
                        bearerAuth(it.accessToken)
                    }
                }
                .body<MastodonAccount>()
                .toProfile(authenticationLock, tootPaginateSource)
        }
    }
}

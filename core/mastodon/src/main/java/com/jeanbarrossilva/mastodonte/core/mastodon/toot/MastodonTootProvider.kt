package com.jeanbarrossilva.mastodonte.core.mastodon.toot

import com.jeanbarrossilva.mastodonte.core.auth.AuthenticationLock
import com.jeanbarrossilva.mastodonte.core.mastodon.Mastodon
import com.jeanbarrossilva.mastodonte.core.mastodon.toot.status.Status
import com.jeanbarrossilva.mastodonte.core.toot.Toot
import com.jeanbarrossilva.mastodonte.core.toot.TootProvider
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MastodonTootProvider(private val authenticationLock: AuthenticationLock) : TootProvider {
    override suspend fun provide(id: String): Flow<Toot> {
        return flow {
            Mastodon
                .HttpClient
                .get("/api/v1/statuses/$id") {
                    authenticationLock.unlock {
                        header(HttpHeaders.Authorization, it.accessToken)
                    }
                }
                .body<Status>()
                .toToot(authenticationLock)
                .also { emit(it) }
        }
    }
}

package com.jeanbarrossilva.mastodonte.core.mastodon.profile.toot

import com.jeanbarrossilva.mastodonte.core.mastodon.client.MastodonHttpClient
import com.jeanbarrossilva.mastodonte.core.mastodon.client.authenticateAndGet
import com.jeanbarrossilva.mastodonte.core.mastodon.profile.toot.status.Status
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.core.profile.toot.TootProvider
import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MastodonTootProvider : TootProvider {
    override suspend fun provide(id: String): Flow<Toot> {
        return flow {
            MastodonHttpClient
                .authenticateAndGet("/api/v1/statuses/$id")
                .body<Status>()
                .toToot()
                .also { emit(it) }
        }
    }
}

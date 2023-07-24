package com.jeanbarrossilva.mastodonte.core.mastodon.feed

import com.jeanbarrossilva.mastodonte.core.auth.AuthenticationLock
import com.jeanbarrossilva.mastodonte.core.auth.actor.Actor
import com.jeanbarrossilva.mastodonte.core.auth.actor.ActorProvider
import com.jeanbarrossilva.mastodonte.core.feed.FeedProvider
import com.jeanbarrossilva.mastodonte.core.mastodon.Mastodon
import com.jeanbarrossilva.mastodonte.core.mastodon.toot.Status
import com.jeanbarrossilva.mastodonte.core.toot.Toot
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MastodonFeedProvider(
    private val actorProvider: ActorProvider,
    private val authenticationLock: AuthenticationLock
) : FeedProvider() {
    override suspend fun onProvide(userID: String, page: Int): Flow<List<Toot>> {
        return authenticationLock.unlock {
            flow {
                Mastodon
                    .HttpClient
                    .get("/api/v1/timelines/home") { header("Authorization", it.accessToken) }
                    .body<List<Status>>()
                    .map { it.toToot(authenticationLock) }
                    .also { emit(it) }
            }
        }
    }

    override suspend fun containsUser(userID: String): Boolean {
        return when (actorProvider.provide()) {
            is Actor.Unauthenticated -> false
            is Actor.Authenticated -> true
        }
    }
}

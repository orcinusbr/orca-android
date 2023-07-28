package com.jeanbarrossilva.mastodonte.core.mastodon.feed

import com.jeanbarrossilva.mastodonte.core.auth.actor.Actor
import com.jeanbarrossilva.mastodonte.core.auth.actor.ActorProvider
import com.jeanbarrossilva.mastodonte.core.feed.FeedProvider
import com.jeanbarrossilva.mastodonte.core.mastodon.toot.status.TootPaginateSource
import com.jeanbarrossilva.mastodonte.core.toot.Toot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MastodonFeedProvider(
    private val actorProvider: ActorProvider,
    private val paginateSource: PaginateSource
) : FeedProvider() {
    private val flow = paginateSource.loadPages(TootPaginateSource.DEFAULT_COUNT).map {
        it.items
    }

    class PaginateSource : TootPaginateSource() {
        override val route = "/api/v1/timelines/home"
    }

    override suspend fun onProvide(userID: String, page: Int): Flow<List<Toot>> {
        paginateSource.paginateTo(page)
        return flow
    }

    override suspend fun containsUser(userID: String): Boolean {
        return when (actorProvider.provide()) {
            is Actor.Unauthenticated -> false
            is Actor.Authenticated -> true
        }
    }
}

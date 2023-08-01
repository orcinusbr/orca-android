package com.jeanbarrossilva.orca.core.mastodon.feed

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.status.TootPaginateSource
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

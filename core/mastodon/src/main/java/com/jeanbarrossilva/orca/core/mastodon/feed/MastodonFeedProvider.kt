package com.jeanbarrossilva.orca.core.mastodon.feed

import com.chrynan.paginate.core.loadAllPagesItems
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.pagination.TootPaginateSource
import kotlinx.coroutines.flow.Flow

class MastodonFeedProvider(private val actorProvider: ActorProvider) : FeedProvider() {
    private val flow = FeedTootPaginateSource.loadAllPagesItems(TootPaginateSource.DEFAULT_COUNT)
    override suspend fun onProvide(userID: String, page: Int): Flow<List<Toot>> {
        FeedTootPaginateSource.paginateTo(page)
        return flow
    }

    override suspend fun containsUser(userID: String): Boolean {
        return when (actorProvider.provide()) {
            is Actor.Unauthenticated -> false
            is Actor.Authenticated -> true
        }
    }
}

package com.jeanbarrossilva.mastodonte.core.mastodon.feed

import com.jeanbarrossilva.mastodonte.core.auth.AuthenticationLock
import com.jeanbarrossilva.mastodonte.core.auth.actor.Actor
import com.jeanbarrossilva.mastodonte.core.auth.actor.ActorProvider
import com.jeanbarrossilva.mastodonte.core.feed.FeedProvider
import com.jeanbarrossilva.mastodonte.core.mastodon.toot.status.StatusPaginateSource
import com.jeanbarrossilva.mastodonte.core.toot.Toot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MastodonFeedProvider(
    private val actorProvider: ActorProvider,
    private val authenticationLock: AuthenticationLock,
    private val paginateSource: PaginateSource
) : FeedProvider() {
    private val flow = paginateSource.loadPages(count = TOOTS_PER_PAGE).map { result ->
        result.items.map { status ->
            status.toToot(authenticationLock)
        }
    }

    class PaginateSource(override val authenticationLock: AuthenticationLock) :
        StatusPaginateSource() {
        override val route = "/api/v1/timelines/home"
    }

    override suspend fun onProvide(userID: String, page: Int): Flow<List<Toot>> {
        paginateTo(page)
        return flow
    }

    override suspend fun containsUser(userID: String): Boolean {
        return when (actorProvider.provide()) {
            is Actor.Unauthenticated -> false
            is Actor.Authenticated -> true
        }
    }

    private suspend fun paginateTo(page: Int) {
        var current = paginateSource.currentPage?.info?.index ?: -1
        while (current != page) {
            if (current < page) {
                paginateSource.next(count = TOOTS_PER_PAGE)
                current++
            } else {
                paginateSource.previous(count = TOOTS_PER_PAGE)
                current--
            }
        }
    }

    companion object {
        private const val TOOTS_PER_PAGE = 20
    }
}

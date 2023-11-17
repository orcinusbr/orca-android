package com.jeanbarrossilva.orca.core.mastodon.feed

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.TermMuter
import kotlinx.coroutines.flow.Flow

/**
 * [FeedProvider] that requests the feed's [Toot]s to the API.
 *
 * @param actorProvider [ActorProvider] by which the current [Actor] will be provided.
 * @param tootPaginator [MastodonFeedPaginator] that will paginate through the [Toot]s in the feed.
 */
class MastodonFeedProvider
internal constructor(
  private val actorProvider: ActorProvider,
  override val termMuter: TermMuter,
  private val tootPaginator: MastodonFeedPaginator
) : FeedProvider() {
  override suspend fun onProvide(userID: String, page: Int): Flow<List<Toot>> {
    return tootPaginator.paginateTo(page)
  }

  override suspend fun containsUser(userID: String): Boolean {
    return when (actorProvider.provide()) {
      is Actor.Unauthenticated -> false
      is Actor.Authenticated -> true
    }
  }
}

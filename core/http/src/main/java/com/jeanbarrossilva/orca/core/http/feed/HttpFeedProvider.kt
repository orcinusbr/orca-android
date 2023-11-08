package com.jeanbarrossilva.orca.core.http.feed

import com.chrynan.paginate.core.loadAllPagesItems
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.TermMuter
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.pagination.HttpTootPaginateSource
import kotlinx.coroutines.flow.Flow

/**
 * [FeedProvider] that requests the feed's [Toot]s to the API.
 *
 * @param actorProvider [ActorProvider] by which the current [Actor] will be provided.
 * @param tootPaginateSource [FeedTootPaginateSource] that will paginate through the [Toot]s in the
 *   feed.
 */
class HttpFeedProvider
internal constructor(
  private val actorProvider: ActorProvider,
  override val termMuter: TermMuter,
  private val tootPaginateSource: FeedTootPaginateSource
) : FeedProvider() {
  /** [Flow] of paginated [Toot]s to be provided. */
  private val flow = tootPaginateSource.loadAllPagesItems(HttpTootPaginateSource.DEFAULT_COUNT)

  override suspend fun onProvide(userID: String, page: Int): Flow<List<Toot>> {
    tootPaginateSource.paginateTo(page)
    return flow
  }

  override suspend fun containsUser(userID: String): Boolean {
    return when (actorProvider.provide()) {
      is Actor.Unauthenticated -> false
      is Actor.Authenticated -> true
    }
  }
}

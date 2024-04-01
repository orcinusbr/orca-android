/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.mastodon.feed

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.TermMuter
import kotlinx.coroutines.flow.Flow

/**
 * [FeedProvider] that requests the feed's [Post]s to the API.
 *
 * @param actorProvider [ActorProvider] by which the current [Actor] will be provided.
 * @param postPaginator [MastodonFeedPaginator] that will paginate through the [Post]s in the feed.
 */
class MastodonFeedProvider
internal constructor(
  private val actorProvider: ActorProvider,
  override val termMuter: TermMuter,
  private val postPaginator: MastodonFeedPaginator
) : FeedProvider() {
  override suspend fun onProvide(userID: String, page: Int): Flow<List<Post>> {
    return postPaginator.paginateTo(page)
  }

  override suspend fun containsUser(userID: String): Boolean {
    return when (actorProvider.provide()) {
      is Actor.Unauthenticated -> false
      is Actor.Authenticated -> true
    }
  }
}

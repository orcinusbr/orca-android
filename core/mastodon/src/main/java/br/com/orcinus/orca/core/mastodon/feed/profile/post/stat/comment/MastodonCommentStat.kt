/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.stat.comment

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.stat.addable.AddableStat
import br.com.orcinus.orca.core.mastodon.feed.profile.post.MastodonPost
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.authenticated
import kotlinx.coroutines.flow.Flow

/**
 * [AddableStat] for adding comments to and removing them from a [MastodonPost]. Both the obtainance
 * that produces the [List] with those already present and the commenting itself are performed
 * through the API.
 *
 * @param count Initial amount of [Profile]s by which the [MastodonPost] has been favorited.
 * @property paginatorProvider Provides the [MastodonCommentPaginator] by which pagination is
 *   performed when the comments are requested to be obtained.
 * @property requester [Requester] by which the network calls are made.
 * @property id ID of the [MastodonPost].
 * @see MastodonPost.id
 */
internal class MastodonCommentStat(
  private val requester: Requester,
  private val paginatorProvider: MastodonCommentPaginator.Provider,
  private val id: String,
  count: Int
) : AddableStat<Post>(count) {
  override suspend fun get(page: Int): Flow<List<Post>> {
    return paginatorProvider.provide(id).paginateTo(page)
  }

  override suspend fun onAddition(element: Post) {
    requester.authenticated().post({ path("api").path("v1").path("statuses").build() }) {
      parameters {
        append("in_reply_to_id", id)
        append("status", "${element.content.text}")
      }
    }
  }

  override suspend fun onRemoval(element: Post) {
    requester
      .authenticated()
      .delete({ path("api").path("v1").path("statuses").path(element.id).build() })
  }
}

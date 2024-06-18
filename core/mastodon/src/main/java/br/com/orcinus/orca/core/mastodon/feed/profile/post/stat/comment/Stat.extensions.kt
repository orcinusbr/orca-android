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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.stat.comment

import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.stat.Stat
import br.com.orcinus.orca.core.feed.profile.post.stat.addable.AddableStat
import br.com.orcinus.orca.core.mastodon.feed.profile.post.MastodonPost
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.authenticated

/**
 * Builds a [Stat] for a [MastodonPost]'s comments that obtains them from the API.
 *
 * @property requester [Requester] by which requests to add or remove comments are performed.
 * @property id ID of the [MastodonPost] for which the [Stat] is.
 * @property count Amount of comments that the [MastodonPost] has received.
 * @property contextPaginatorProvider [MastodonCommentPaginator.Provider] by which a
 *   [MastodonCommentPaginator] for paginating through the comments will be provided.
 * @see Post.comment
 */
@Suppress("FunctionName")
internal fun CommentStat(
  requester: Requester,
  id: String,
  count: Int,
  contextPaginatorProvider: MastodonCommentPaginator.Provider
): AddableStat<Post> {
  return AddableStat(count) {
    contextPaginatorProvider.provide(id).let { contextPaginator ->
      get { index -> contextPaginator.paginateTo(index) }
    }
    onAdd {
      requester.authenticated().post({ path("api").path("v1").path("statuses").build() }) {
        parameters {
          append("in_reply_to_id", id)
          append("status", "${it.content.text}")
        }
      }
    }
    onRemove {
      requester
        .authenticated()
        .delete({ path("api").path("v1").path("statuses").path(it.id).build() })
    }
  }
}

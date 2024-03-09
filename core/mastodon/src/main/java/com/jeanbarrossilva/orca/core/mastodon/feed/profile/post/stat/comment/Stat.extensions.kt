/*
 * Copyright © 2023-2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.stat.comment

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.Stat
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.addable.AddableStat
import com.jeanbarrossilva.orca.core.mastodon.client.authenticateAndDelete
import com.jeanbarrossilva.orca.core.mastodon.client.authenticateAndSubmitForm
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.MastodonPost
import com.jeanbarrossilva.orca.core.mastodon.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.instanceProvider
import com.jeanbarrossilva.orca.std.injector.Injector
import io.ktor.http.Parameters

/**
 * Builds a [Stat] for an [MastodonPost]'s comments that obtains them from the API.
 *
 * @param id ID of the [MastodonPost] for which the [Stat] is.
 * @param count Amount of comments that the [MastodonPost] has received.
 * @param contextPaginatorProvider [MastodonCommentPaginator.Provider] by which a
 *   [MastodonCommentPaginator] for paginating through the comments will be provided.
 * @see Post.comment
 */
@Suppress("FunctionName")
internal fun CommentStat(
  id: String,
  count: Int,
  contextPaginatorProvider: MastodonCommentPaginator.Provider
): AddableStat<Post> {
  return AddableStat(count) {
    contextPaginatorProvider.provide(id).let { contextPaginator ->
      get { index -> contextPaginator.paginateTo(index) }
    }
    add {
      (Injector.from<CoreModule>().instanceProvider().provide() as SomeHttpInstance)
        .client
        .authenticateAndSubmitForm(
          "/api/v1/statuses",
          Parameters.build {
            append("in_reply_to_id", id)
            append("status", "${it.content.text}")
          }
        )
    }
    remove {
      (Injector.from<CoreModule>().instanceProvider().provide() as SomeHttpInstance)
        .client
        .authenticateAndDelete("/api/v1/statuses/${it.id}")
    }
  }
}

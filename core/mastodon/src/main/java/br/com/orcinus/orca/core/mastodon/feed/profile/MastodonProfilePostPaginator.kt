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

package br.com.orcinus.orca.core.mastodon.feed.profile

import android.content.Context
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.MastodonPostPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.MastodonStatusesPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.post.stat.comment.MastodonCommentPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import java.net.URI

/**
 * [MastodonPostPaginator] that paginates through a [MastodonAccount]'s [MastodonStatus]es.
 *
 * @property id ID of the [MastodonAccount].
 * @see MastodonAccount.id
 */
internal class MastodonProfilePostPaginator(
  override val context: Context,
  override val requester: Requester,
  override val actorProvider: ActorProvider,
  override val commentPaginatorProvider: MastodonCommentPaginator.Provider,
  override val imageLoaderProvider: SomeImageLoaderProvider<URI>,
  private val id: String
) : MastodonStatusesPaginator() {
  /** Provides a [MastodonProfilePostPaginator] through [provide]. */
  fun interface Provider {
    /**
     * Provides a [MastodonProfilePostPaginator] that paginates through the [MastodonStatus]es of a
     * [MastodonAccount] identified as [id].
     *
     * @param id ID of the [MastodonAccount].
     * @see MastodonAccount.id
     */
    fun provide(id: String): MastodonProfilePostPaginator
  }

  override fun HostedURLBuilder.buildInitialRoute(): URI {
    return path("api").path("v1").path("accounts").path(id).path("statuses").build()
  }

  override fun List<MastodonStatus>.toPosts(): List<Post> {
    return map {
      it.toPost(
        context,
        requester,
        actorProvider,
        profilePostPaginatorProvider = { this@MastodonProfilePostPaginator },
        commentPaginatorProvider,
        imageLoaderProvider
      )
    }
  }
}

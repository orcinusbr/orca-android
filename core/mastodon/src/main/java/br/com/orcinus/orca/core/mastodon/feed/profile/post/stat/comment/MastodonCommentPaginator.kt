/*
 * Copyright © 2024–2025 Orcinus
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

import android.content.Context
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.post.MastodonContext
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.MastodonPostPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.type.KTypeCreator
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.type.kTypeCreatorOf
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import java.net.URI

/**
 * [MastodonPostPaginator] for paginating through the comments of a [Post].
 *
 * @property context [Context] with which [MastodonContext]s will be converted into [Post]s.
 * @property actorProvider [ActorProvider] for determining whether ownership of [Post]s can be given
 *   to the current [Actor].
 * @property profilePostPaginatorProvider Paginates through the [Post]s of converted [Profile]s.
 * @property imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   images will be loaded from a [URI].
 * @property id ID of the original [Post].
 * @see Post.comment
 * @see Post.id
 * @see toPosts
 * @see Post.own
 */
internal class MastodonCommentPaginator(
  private val context: Context,
  override val requester: Requester<*>,
  private val actorProvider: ActorProvider,
  private val profilePostPaginatorProvider: MastodonProfilePostPaginator.Provider,
  private val imageLoaderProvider: SomeImageLoaderProvider<URI>,
  private val id: String
) : MastodonPostPaginator<MastodonContext>(), KTypeCreator<MastodonContext> by kTypeCreatorOf() {
  /** Provides a [MastodonProfilePostPaginator] through [provide]. */
  fun interface Provider {
    /**
     * Provides a [MastodonCommentPaginator] that paginates through the comments of a [Post]
     * identified as [id].
     *
     * @param id ID of the original [Post].
     * @see Post.comment
     * @see Post.id
     */
    fun provide(id: String): MastodonCommentPaginator
  }

  override fun HostedURLBuilder.buildInitialRoute(): URI {
    return path("api").path("v1").path("statuses").path(id).path("context").build()
  }

  override fun MastodonContext.toPosts(): List<Post> {
    return descendants.map {
      it.toPost(
        context,
        requester,
        actorProvider,
        profilePostPaginatorProvider,
        commentPaginatorProvider = { this@MastodonCommentPaginator },
        imageLoaderProvider
      )
    }
  }
}

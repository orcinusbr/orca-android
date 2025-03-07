/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.cache

import android.content.Context
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.stat.Stat
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.post.stat.comment.MastodonCommentPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.authenticated
import br.com.orcinus.orca.platform.cache.Fetcher
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import io.ktor.client.call.body
import java.net.URI

/**
 * [Fetcher] that requests [Post]s to the API.
 *
 * @property context [Context] with which a fetched [MastodonStatus] will be converted into a
 *   [Post].
 * @property requester [Requester] by which [Stat]-related requests are performed.
 * @property actorProvider [ActorProvider] for determining whether ownership of [Post]s can be given
 *   to the current [Actor].
 * @property profilePostPaginatorProvider Paginates through the [Post]s of [Profile]s that are
 *   obtained by the [Stat]s.
 * @property commentPaginatorProvider [MastodonCommentPaginator] by which a
 *   [MastodonCommentPaginator] for paginating through the fetched [Post]s will be provided.
 * @property imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   images will be loaded from a [URI].
 * @see MastodonStatus.toPost
 * @see Post.own
 * @see Post.comment
 */
internal class MastodonPostFetcher(
  private val context: Context,
  private val requester: Requester<*>,
  private val actorProvider: ActorProvider,
  private val profilePostPaginatorProvider: MastodonProfilePostPaginator.Provider,
  private val commentPaginatorProvider: MastodonCommentPaginator.Provider,
  private val imageLoaderProvider: SomeImageLoaderProvider<URI>
) : Fetcher<Post>() {
  override suspend fun onFetch(key: String) =
    requester
      .authenticated()
      .get({ path("api").path("v1").path("statuses").path(key).build() })
      .map {
        it
          .body<MastodonStatus>()
          .toPost(
            context,
            requester,
            actorProvider,
            profilePostPaginatorProvider,
            commentPaginatorProvider,
            imageLoaderProvider
          )
      }
      .getValueOrThrow()
}

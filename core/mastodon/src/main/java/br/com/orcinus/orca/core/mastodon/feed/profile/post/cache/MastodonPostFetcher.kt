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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.cache

import android.content.Context
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.mastodon.feed.profile.post.stat.comment.MastodonCommentPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import br.com.orcinus.orca.core.mastodon.instance.SomeMastodonInstance
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.authenticated
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.instanceProvider
import br.com.orcinus.orca.platform.cache.Fetcher
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import br.com.orcinus.orca.std.injector.Injector
import io.ktor.client.call.body
import java.net.URI

/**
 * [Fetcher] that requests [Post]s to the API.
 *
 * @param context [Context] with which a fetched [MastodonStatus] will be converted into a [Post].
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which images
 *   will be loaded from a [URI].
 * @param commentPaginatorProvider [MastodonCommentPaginator] by which a [MastodonCommentPaginator]
 *   for paginating through the fetched [Post]s will be provided.
 * @see MastodonStatus.toPost
 * @see Post.comment
 */
internal class MastodonPostFetcher(
  private val context: Context,
  private val imageLoaderProvider: SomeImageLoaderProvider<URI>,
  private val commentPaginatorProvider: MastodonCommentPaginator.Provider
) : Fetcher<Post>() {
  override suspend fun onFetch(key: String): Post {
    return (Injector.from<CoreModule>().instanceProvider().provide() as SomeMastodonInstance)
      .requester
      .authenticated()
      .get({ path("api").path("v1").path("statuses").path(key).build() })
      .body<MastodonStatus>()
      .toPost(context, imageLoaderProvider, commentPaginatorProvider)
  }
}

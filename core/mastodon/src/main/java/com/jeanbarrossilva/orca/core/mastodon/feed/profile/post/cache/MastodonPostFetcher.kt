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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.cache

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.mastodon.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.stat.comment.MastodonCommentPaginator
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import com.jeanbarrossilva.orca.core.mastodon.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.instanceProvider
import com.jeanbarrossilva.orca.platform.cache.Fetcher
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.SomeImageLoaderProvider
import com.jeanbarrossilva.orca.std.injector.Injector
import io.ktor.client.call.body
import java.net.URL

/**
 * [Fetcher] that requests [Post]s to the API.
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which images
 *   will be loaded from a [URL].
 * @param commentPaginatorProvider [MastodonCommentPaginator] by which a [MastodonCommentPaginator]
 *   for paginating through the fetched [Post]s will be provided.
 * @see Post.comment
 */
internal class MastodonPostFetcher(
  private val imageLoaderProvider: SomeImageLoaderProvider<URL>,
  private val commentPaginatorProvider: MastodonCommentPaginator.Provider
) : Fetcher<Post>() {
  override suspend fun onFetch(key: String): Post {
    return (Injector.from<CoreModule>().instanceProvider().provide() as SomeHttpInstance)
      .client
      .authenticateAndGet("/api/v1/statuses/$key")
      .body<MastodonStatus>()
      .toPost(imageLoaderProvider, commentPaginatorProvider)
  }
}

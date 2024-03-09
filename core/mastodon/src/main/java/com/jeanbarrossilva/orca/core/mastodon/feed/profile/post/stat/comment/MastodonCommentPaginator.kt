/*
 * Copyright © 2024 Orca
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
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.MastodonContext
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination.KTypeCreator
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination.MastodonPostPaginator
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.SomeImageLoaderProvider
import java.net.URL
import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * [MastodonPostPaginator] for paginating through the comments of a [Post].
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which images
 *   will be loaded from a [URL].
 * @param id ID of the original [Post].
 * @see Post.comment
 * @see Post.id
 */
internal class MastodonCommentPaginator(
  private val imageLoaderProvider: SomeImageLoaderProvider<URL>,
  id: String
) : MastodonPostPaginator<MastodonContext>() {
  override val route = "/api/v1/statuses/$id/context"
  override val dtoClass = MastodonContext::class

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

  override fun create(kClass: KClass<*>): KType {
    return KTypeCreator.defaultFor<MastodonContext>().create(kClass)
  }

  override fun MastodonContext.toMastodonPosts(): List<Post> {
    return descendants.map { it.toPost(imageLoaderProvider) { this@MastodonCommentPaginator } }
  }
}

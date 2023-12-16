/*
 * Copyright © 2023 Orca
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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post

import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.stat.CommentStat
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.stat.FavoriteStat
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.stat.ReblogStat
import com.jeanbarrossilva.orca.std.image.Image
import com.jeanbarrossilva.orca.std.image.ImageLoader
import java.net.URL
import java.time.ZonedDateTime

/**
 * [Post] whose actions communicate with the Mastodon API.
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s will be loaded from a [URL].
 * @param commentCount Amount of comments that this [MastodonPost] has received.
 * @param favoriteCount Amount of times that this [MastodonPost] has been marked as favorite.
 * @param reblogCount Amount of times that this [MastodonPost] has been reblogged.
 */
data class MastodonPost
internal constructor(
  override val id: String,
  override val author: Author,
  override val content: Content,
  private val imageLoaderProvider: ImageLoader.Provider<URL>,
  override val publicationDateTime: ZonedDateTime,
  private val commentCount: Int,
  private val favoriteCount: Int,
  private val reblogCount: Int,
  override val url: URL
) : Post() {
  override val comment = CommentStat(id, commentCount, imageLoaderProvider)
  override val favorite = FavoriteStat(id, favoriteCount)
  override val repost = ReblogStat(id, reblogCount)
}

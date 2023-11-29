package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post

import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.stat.CommentStat
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.stat.FavoriteStat
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.stat.ReblogStat
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
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

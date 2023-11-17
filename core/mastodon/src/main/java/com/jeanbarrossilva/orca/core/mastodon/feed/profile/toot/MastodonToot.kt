package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.stat.CommentStat
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.stat.FavoriteStat
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.stat.ReblogStat
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL
import java.time.ZonedDateTime

/**
 * [Toot] whose actions communicate with the Mastodon API.
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s will be loaded from a [URL].
 * @param commentCount Amount of comments that this [MastodonToot] has received.
 * @param favoriteCount Amount of times that this [MastodonToot] has been marked as favorite.
 * @param reblogCount Amount of times that this [MastodonToot] has been reblogged.
 */
data class MastodonToot
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
) : Toot() {
  override val comment = CommentStat(id, commentCount, imageLoaderProvider)
  override val favorite = FavoriteStat(id, favoriteCount)
  override val reblog = ReblogStat(id, reblogCount)
}

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

package br.com.orcinus.orca.core.mastodon.feed.profile.post

import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.mastodon.feed.profile.post.stat.FavoriteStat
import br.com.orcinus.orca.core.mastodon.feed.profile.post.stat.RepostStat
import br.com.orcinus.orca.core.mastodon.feed.profile.post.stat.comment.CommentStat
import br.com.orcinus.orca.core.mastodon.feed.profile.post.stat.comment.MastodonCommentPaginator
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import java.net.URI
import java.time.ZonedDateTime

/**
 * [Post] whose actions communicate with the Mastodon API.
 *
 * @property requester [Requester] by which [comment]-, [favorite]- and [repost]-related requests
 *   are performed.
 * @property imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   images will be loaded from a [URI].
 * @property commentPaginatorProvider [MastodonCommentPaginator.Provider] by which a
 *   [MastodonCommentPaginator] for paginating through the comments will be provided.
 * @property commentCount Amount of comments that this [MastodonPost] has received.
 * @property favoriteCount Amount of times that this [MastodonPost] has been marked as favorite.
 * @property reblogCount Amount of times that this [MastodonPost] has been reblogged.
 * @see comment
 */
class MastodonPost
internal constructor(
  internal val requester: Requester,
  override val actorProvider: ActorProvider,
  private val imageLoaderProvider: SomeImageLoaderProvider<URI>,
  override val id: String,
  override val author: Author,
  override val content: Content,
  override val publicationDateTime: ZonedDateTime,
  private val commentPaginatorProvider: MastodonCommentPaginator.Provider,
  private val commentCount: Int,
  private val favoriteCount: Int,
  private val reblogCount: Int,
  override val uri: URI
) : Post() {
  override val comment = CommentStat(requester, id, commentCount, commentPaginatorProvider)
  override val favorite = FavoriteStat(requester, id, favoriteCount)
  override val repost = RepostStat(requester, id, reblogCount)

  override suspend fun toOwnedPost(): MastodonOwnedPost {
    return MastodonOwnedPost(this)
  }
}

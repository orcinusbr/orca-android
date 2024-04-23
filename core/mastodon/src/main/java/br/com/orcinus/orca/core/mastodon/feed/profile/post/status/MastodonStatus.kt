/*
 * Copyright © 2023-2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.status

import br.com.orcinus.orca.composite.timeline.text.annotated.fromHtml
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.repost.Repost
import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import br.com.orcinus.orca.core.mastodon.feed.profile.post.MastodonPost
import br.com.orcinus.orca.core.mastodon.feed.profile.post.stat.comment.MastodonCommentPaginator
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.instanceProvider
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.`if`
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.markdown.Markdown
import java.net.URL
import java.time.ZonedDateTime
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API that is the DTO version of either A [MastodonPost] or a [Repost];
 * which one it represents is determined by [reblog]'s nullability.
 *
 * @param id Unique identifier.
 * @param createdAt ISO-8601-formatted [String] indicating the date and time of creation.
 * @param account [MastodonAccount] of the author that's created this [MastodonStatus].
 * @param reblogsCount Amount of times it's been reblogged.
 * @param favouritesCount Amount of times it's been favorited.
 * @param repliesCount Amount of replies that's been received.
 * @param url String [URL] that leads to this [MastodonStatus].
 * @param reblog [MastodonStatus] that's being reblogged in this one.
 * @param card [MastodonCard] for the highlighted [URL] that's in the [content].
 * @param content Text that's been written.
 * @param mediaAttachments [MastodonAttachment]s of attached media.
 * @param favourited Whether it's been favorited by the currently
 *   [authenticated][Actor.Authenticated] [Actor].
 * @param reblogged Whether the [authenticated][Actor.Authenticated] [Actor] has reblogged this
 *   [MastodonStatus].
 */
@Serializable
data class MastodonStatus
internal constructor(
  internal val id: String,
  internal val createdAt: String,
  internal val account: MastodonAccount,
  internal val reblogsCount: Int,
  internal val favouritesCount: Int,
  internal val repliesCount: Int,
  internal val url: String,
  internal val reblog: MastodonStatus?,
  internal val card: MastodonCard?,
  internal val content: String,
  internal val mediaAttachments: List<MastodonAttachment>,
  internal val favourited: Boolean?,
  internal val reblogged: Boolean?
) {
  /**
   * Converts this [MastodonStatus] into a [Post].
   *
   * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
   *   images will be loaded from a [URL].
   * @param commentPaginatorProvider [MastodonCommentPaginator.Provider] by which a
   *   [MastodonCommentPaginator] for paginating through the comments will be provided.
   * @see Post.comment
   */
  internal fun toPost(
    imageLoaderProvider: SomeImageLoaderProvider<URL>,
    commentPaginatorProvider: MastodonCommentPaginator.Provider
  ): Post {
    val author =
      reblog?.account?.toAuthor(imageLoaderProvider) ?: account.toAuthor(imageLoaderProvider)
    val domain = Injector.from<CoreModule>().instanceProvider().provide().domain
    val text = Markdown.fromHtml(reblog?.content ?: content)
    val attachments =
      (reblog?.mediaAttachments ?: mediaAttachments).map(MastodonAttachment::toAttachment)
    val content = Content.from(domain, text, attachments) { card?.toHeadline(imageLoaderProvider) }
    val publicationDateTime = ZonedDateTime.parse(reblog?.createdAt ?: createdAt)
    val url = URL(reblog?.url ?: url)
    return MastodonPost(
        id,
        imageLoaderProvider,
        author,
        content,
        publicationDateTime,
        commentPaginatorProvider,
        commentCount = reblog?.repliesCount ?: repliesCount,
        favouritesCount,
        reblogsCount,
        url
      )
      .`if`<Post>(reblog != null) {
        val reposter = this@MastodonStatus.account.toAuthor(imageLoaderProvider)
        Repost(this, reposter)
      }
  }
}

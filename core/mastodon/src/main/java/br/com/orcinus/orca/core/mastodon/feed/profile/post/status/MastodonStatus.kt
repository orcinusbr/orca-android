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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.status

import android.content.Context
import androidx.annotation.VisibleForTesting
import br.com.orcinus.orca.composite.timeline.text.annotated.fromHtml
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.repost.Repost
import br.com.orcinus.orca.core.feed.profile.post.stat.Stat
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import br.com.orcinus.orca.core.mastodon.feed.profile.post.MastodonPost
import br.com.orcinus.orca.core.mastodon.feed.profile.post.stat.comment.MastodonCommentPaginator
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.instanceProvider
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.`if`
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.`if`
import java.net.URI
import java.net.URL
import java.time.ZonedDateTime
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API that is the DTO version of either a [MastodonPost] or a [Repost];
 * which one it represents is determined by [reblog]'s nullability.
 *
 * @property id Unique identifier.
 * @property createdAt ISO-8601-formatted [String] indicating the date and time of creation.
 * @property account [MastodonAccount] of the author that's created this [MastodonStatus].
 * @property reblogsCount Amount of times it's been reblogged.
 * @property favouritesCount Amount of times it's been favorited.
 * @property repliesCount Amount of replies that's been received.
 * @property uri String [URL] that leads to this [MastodonStatus].
 * @property reblog [MastodonStatus] that's being reblogged in this one.
 * @property card [MastodonCard] for the highlighted [URI] that's in the [content].
 * @property content Text that's been written.
 * @property mediaAttachments [MastodonAttachment]s of attached media.
 * @property favourited Whether it's been favorited by the currently
 *   [authenticated][Actor.Authenticated] [Actor].
 * @property reblogged Whether the [authenticated][Actor.Authenticated] [Actor] has reblogged this
 *   [MastodonStatus].
 */
@Serializable
internal data class MastodonStatus(
  private val id: String,
  private val createdAt: String,
  private val account: MastodonAccount,
  private val reblogsCount: Int,
  private val favouritesCount: Int,
  private val repliesCount: Int,
  private val uri: String,
  private val reblog: MastodonStatus?,
  private val card: MastodonCard?,
  private val content: String,
  private val mediaAttachments: List<MastodonAttachment>,
  val favourited: Boolean?,
  val reblogged: Boolean?
) {
  /** Variation of the [content] which can contain a maximum of only 12 [Char]s. */
  val summarizedContent
    get() =
      content.substring(startIndex = 0, endIndex = minOf(11, content.length)).`if`({
        length < content.length
      }) {
        plus('…')
      }

  /**
   * Converts this [MastodonStatus] into a [Post].
   *
   * @param context [Context] with which the [content] will be converted into [Markdown].
   * @param requester [Requester] by which [Stat]-related requests are performed.
   * @param actorProvider [ActorProvider] for determining whether ownership of the resulting [Post]
   *   can be given to the current [Actor].
   * @param profilePostPaginatorProvider Paginates through the [Post]s of [Profile]s that are
   *   obtained by the [Stat]s.
   * @param commentPaginatorProvider [MastodonCommentPaginator.Provider] by which a
   *   [MastodonCommentPaginator] for paginating through the comments will be provided.
   * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
   *   images will be loaded from a [URI].
   * @see Markdown.Companion.fromHtml
   * @see Post.own
   * @see Post.comment
   */
  internal fun toPost(
    context: Context,
    requester: Requester,
    actorProvider: ActorProvider,
    profilePostPaginatorProvider: MastodonProfilePostPaginator.Provider,
    commentPaginatorProvider: MastodonCommentPaginator.Provider,
    imageLoaderProvider: SomeImageLoaderProvider<URI>
  ): Post {
    val author =
      reblog?.account?.toAuthor(imageLoaderProvider) ?: account.toAuthor(imageLoaderProvider)
    val domain = Injector.from<CoreModule>().instanceProvider().provide().domain
    val text = Markdown.fromHtml(context, reblog?.content ?: content)
    val attachments =
      (reblog?.mediaAttachments ?: mediaAttachments).map(MastodonAttachment::toAttachment)
    val content = Content.from(domain, text, attachments) { card?.toHeadline(imageLoaderProvider) }
    val publicationDateTime = ZonedDateTime.parse(reblog?.createdAt ?: createdAt)
    val uri = URI(reblog?.uri ?: uri)
    return MastodonPost(
        context,
        requester,
        actorProvider,
        profilePostPaginatorProvider,
        imageLoaderProvider,
        id,
        author,
        content,
        publicationDateTime,
        commentPaginatorProvider,
        commentCount = reblog?.repliesCount ?: repliesCount,
        isFavorited = favourited ?: false,
        favouritesCount,
        isReposted = reblogged ?: false,
        reblogsCount,
        uri
      )
      .`if`<Post>(reblog != null) {
        val reposter = this@MastodonStatus.account.toAuthor(imageLoaderProvider)
        Repost(this, reposter)
      }
  }

  companion object {
    /**
     * [MastodonStatus] whose fields are assigned default values (empty [String]s, zeroed [Number]s
     * and `false` [Boolean]s).
     */
    @JvmStatic
    @VisibleForTesting
    val default =
      MastodonStatus(
        id = "0",
        createdAt = "",
        MastodonAccount.default,
        reblogsCount = 0,
        favouritesCount = 0,
        repliesCount = 0,
        uri = "",
        reblog = null,
        card = null,
        content = "",
        mediaAttachments = emptyList(),
        favourited = false,
        reblogged = false
      )
  }
}

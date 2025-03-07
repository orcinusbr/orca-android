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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.cache.storage

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Headline
import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Highlight
import br.com.orcinus.orca.core.feed.profile.post.repost.Repost
import br.com.orcinus.orca.core.feed.profile.post.stat.Stat
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import br.com.orcinus.orca.core.mastodon.feed.profile.cache.storage.style.MastodonStyleEntity
import br.com.orcinus.orca.core.mastodon.feed.profile.post.MastodonPost
import br.com.orcinus.orca.core.mastodon.feed.profile.post.stat.comment.MastodonCommentPaginator
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.instanceProvider
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.`if`
import br.com.orcinus.orca.platform.cache.Cache
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.markdown.Markdown
import java.net.URI
import java.time.ZonedDateTime

/**
 * Primitive information to be persisted about a [Post].
 *
 * @param id Unique identifier.
 * @param authorID ID of the [Author] that has authored the [Post].
 * @param reposterID ID of the [Author] that has reblogged the [Post].
 * @param headlineTitle Title of the [Post]'s [content][Post.content]'s
 *   [highlight][Content.highlight] [headline][Highlight.headline].
 * @param headlineSubtitle Subtitle of the [Post]'s [content][Post.content]'s
 *   [highlight][Content.highlight] [headline][Highlight.headline].
 * @param headlineCoverURI URI [String] that leads to the cover image of the [Post]'s
 *   [content][Post.content]'s [highlight][Content.highlight] [headline][Highlight.headline].
 * @param publicationDateTime [String] representation of the moment in which the [Post] was
 *   published.
 * @param commentCount Amount of comments that the [Post] has received.
 * @param isFavorited Whether the [Post] has been favorited by the currently
 *   [authenticated][Actor.Authenticated] [Actor].
 * @param isReposted Whether the [Post] is reblogged.
 * @param repostCount Amount of times the [Post] has been reblogged.
 * @param uri URI [String] that leads to the [Post].
 */
@Entity(tableName = "posts")
internal data class MastodonPostEntity(
  @PrimaryKey val id: String,
  @ColumnInfo(name = "author_id") val authorID: String,
  @ColumnInfo(name = "reposter_id") val reposterID: String?,
  val text: String,
  @ColumnInfo(name = "headline_title") val headlineTitle: String?,
  @ColumnInfo(name = "headline_subtitle") val headlineSubtitle: String?,
  @ColumnInfo(name = "headline_cover_uri") val headlineCoverURI: String?,
  @ColumnInfo(name = "publication_date_time") val publicationDateTime: String,
  @ColumnInfo(name = "comment_count") val commentCount: Int,
  @ColumnInfo(name = "is_favorited") val isFavorited: Boolean,
  @ColumnInfo(name = "favorite_count") val favoriteCount: Int,
  @ColumnInfo(name = "is_reposted") val isReposted: Boolean,
  @ColumnInfo(name = "repost_count") val repostCount: Int,
  @ColumnInfo(name = "uri") val uri: String
) {
  /**
   * Converts this [MastodonPostEntity] into a [Post].
   *
   * @param context [Context] for converting [MastodonAccount]s fetched by the [Stat]s into
   *   [Profile]s.
   * @param requester [Requester] by which [Stat]-related requests are performed.
   * @param profileCache [Cache] from which the [Author]'s [Profile] will be retrieved.
   * @param dao [MastodonPostEntityDao] that will select the persisted
   *   [Mastodon style entities][MastodonStyleEntity].
   * @param actorProvider [ActorProvider] for determining whether ownership of the resulting [Post]
   *   can be given to the current [Actor].
   * @param profilePostPaginatorProvider Paginates through the [Post]s of [Profile]s that are
   *   obtained by the [Stat]s.
   * @param commentPaginatorProvider [MastodonCommentPaginator.Provider] by which a
   *   [MastodonCommentPaginator] for paginating through the [Post]'s comments will be provided.
   * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by images
   *   will be loaded from a [URI].
   * @see Post.own
   * @see Post.comment
   */
  suspend fun toPost(
    context: Context,
    requester: Requester<*>,
    profileCache: Cache<Profile>,
    dao: MastodonPostEntityDao,
    actorProvider: ActorProvider,
    profilePostPaginatorProvider: MastodonProfilePostPaginator.Provider,
    commentPaginatorProvider: MastodonCommentPaginator.Provider,
    imageLoaderProvider: SomeImageLoaderProvider<URI>
  ): Post {
    val author = profileCache.get(authorID).toAuthor()
    val domain = Injector.from<CoreModule>().instanceProvider().provide().domain
    val styles = dao.selectWithStylesByID(id).styles.map(MastodonStyleEntity::toStyle)
    val text = Markdown.styled(text, styles)
    val coverLoader = headlineCoverURI?.let { imageLoaderProvider.provide(URI(it)) }
    val content =
      Content.from(domain, text) {
        if (headlineTitle != null) {
          Headline(headlineTitle, headlineSubtitle, coverLoader)
        } else {
          null
        }
      }
    val publicationDateTime = ZonedDateTime.parse(publicationDateTime)
    val uri = URI(uri)
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
        commentCount,
        isFavorited,
        favoriteCount,
        isReposted,
        repostCount,
        uri
      )
      .`if`<Post>(reposterID != null) {
        val reposter = profileCache.get(reposterID!!).toAuthor()
        Repost(this, reposter)
      }
  }

  companion object {
    /**
     * Creates a [MastodonPostEntity] from the given [post].
     *
     * @param post [Post] from which the [MastodonPostEntity] will be created.
     */
    fun from(post: Post): MastodonPostEntity {
      return MastodonPostEntity(
        post.id,
        post.author.id,
        reposterID = if (post is Repost) post.reposter.id else null,
        "${post.content.text}",
        post.content.highlight?.headline?.title,
        post.content.highlight?.headline?.subtitle,
        headlineCoverURI =
          post.content.highlight?.headline?.coverLoader?.source?.let { it as? URI }?.toString(),
        "${post.publicationDateTime}",
        post.comment.count,
        post.favorite.isEnabled,
        post.favorite.count,
        post.repost.isEnabled,
        post.repost.count,
        "${post.uri}"
      )
    }
  }
}

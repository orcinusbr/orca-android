/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.status

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.post.repost.Repost
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.account.MastodonAccount
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.MastodonPost
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.instanceProvider
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.`if`
import com.jeanbarrossilva.orca.platform.ui.core.style.fromHtml
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.injector.Injector
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL
import java.time.ZonedDateTime
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API that is the DTO version of either an [MastodonPost] or a [Repost];
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
   *   [Image]s will be loaded from a [URL].
   */
  internal fun toPost(imageLoaderProvider: ImageLoader.Provider<URL>): Post {
    val author =
      reblog?.account?.toAuthor(imageLoaderProvider) ?: account.toAuthor(imageLoaderProvider)
    val domain = Injector.from<CoreModule>().instanceProvider().provide().domain
    val text = StyledString.fromHtml(reblog?.content ?: content)
    val attachments =
      (reblog?.mediaAttachments ?: mediaAttachments).map(MastodonAttachment::toAttachment)
    val content = Content.from(domain, text, attachments) { card?.toHeadline(imageLoaderProvider) }
    val publicationDateTime = ZonedDateTime.parse(reblog?.createdAt ?: createdAt)
    val url = URL(reblog?.url ?: url)
    return MastodonPost(
        id,
        author,
        content,
        imageLoaderProvider,
        publicationDateTime,
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

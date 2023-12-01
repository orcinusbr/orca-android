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

package com.jeanbarrossilva.orca.core.feed.profile.post.repost

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.Stat
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import java.net.URL
import java.time.ZonedDateTime

/**
 * [Post] that has been reposted by someone else.
 *
 * @param original [Post] from which the [Repost] derives.
 * @param reblogger [Author] by which the [Post] has been reblogged.
 */
fun Repost(original: Post, reblogger: Author): Repost {
  return Repost(
    original.id,
    original.author,
    reblogger,
    original.content,
    original.publicationDateTime,
    original.comment,
    original.favorite,
    original.repost,
    original.url
  )
}

/**
 * [Post] that has been reposted by someone else.
 *
 * @param id Unique identifier.
 * @param author [Author] that has authored the [Post].
 * @param reblogger [Author] by which the [Post] has been reblogged.
 * @param content [Content] that's been composed by the [author].
 * @param publicationDateTime Zoned moment in time in which the [Post] was published.
 * @param comment [Stat] for the [Post]'s comments.
 * @param favorite [Stat] for the [Post]'s favorites.
 * @param reblog [Stat] for the [Post]'s reblogs.
 * @param url [URL] that leads to the [Post].
 */
fun Repost(
  id: String,
  author: Author,
  reblogger: Author,
  content: Content,
  publicationDateTime: ZonedDateTime,
  comment: Stat<Post>,
  favorite: ToggleableStat<Profile>,
  reblog: ToggleableStat<Profile>,
  url: URL
): Repost {
  return object : Repost() {
    override val id = id
    override val author = author
    override val reposter = reblogger
    override val content = content
    override val publicationDateTime = publicationDateTime
    override val comment = comment
    override val favorite = favorite
    override val repost = reblog
    override val url = url
  }
}

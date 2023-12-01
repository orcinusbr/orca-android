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

package com.jeanbarrossilva.orca.core.feed.profile.post

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.Stat
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import java.io.Serializable
import java.net.URL
import java.time.ZonedDateTime

/** Content that's been posted by a user, the [author]. */
abstract class Post : Serializable {
  /** Unique identifier. */
  abstract val id: String

  /** [Author] that has authored this [Post]. */
  abstract val author: Author

  /** [Content] that's been composed by the [author]. */
  abstract val content: Content

  /** Zoned moment in time in which this [Post] was published. */
  abstract val publicationDateTime: ZonedDateTime

  /** [Stat] for comments. */
  abstract val comment: Stat<Post>

  /** [Stat] for favorites. */
  abstract val favorite: ToggleableStat<Profile>

  /** [Stat] for reposts. */
  abstract val repost: ToggleableStat<Profile>

  /** [URL] that leads to this [Post]. */
  abstract val url: URL

  companion object
}

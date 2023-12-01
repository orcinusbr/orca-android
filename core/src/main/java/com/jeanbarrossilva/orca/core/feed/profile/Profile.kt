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

package com.jeanbarrossilva.orca.core.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.io.Serializable
import java.net.URL
import kotlinx.coroutines.flow.Flow

/** A user's profile. */
interface Profile : Serializable {
  /** Unique identifier. */
  val id: String

  /** Unique identifier within an instance. */
  val account: Account

  /** [ImageLoader] that loads the avatar. */
  val avatarLoader: SomeImageLoader

  /** Name to be displayed. */
  val name: String

  /** Describes who the owner is and/or provides information regarding this [Profile]. */
  val bio: StyledString

  /** Amount of followers. */
  val followerCount: Int

  /** Amount of following. */
  val followingCount: Int

  /**
   * [URL] that leads to the webpage of the instance through which this [Profile] can be accessed.
   */
  val url: URL

  /**
   * Gets the [Post]s that have been published by the owner of this [Profile].
   *
   * @param page Page in which the [Post]s to be obtained are.
   */
  suspend fun getPosts(page: Int): Flow<List<Post>>

  companion object
}

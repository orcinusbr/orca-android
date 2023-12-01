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

import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import java.util.Objects

/** [Post] that has been reposted by someone else. */
abstract class Repost internal constructor() : Post() {
  /** [Author] by which this [Repost] has been created. */
  abstract val reposter: Author

  override fun equals(other: Any?): Boolean {
    return other is Repost &&
      id == other.id &&
      author == other.author &&
      reposter == other.reposter &&
      content == other.content &&
      publicationDateTime == other.publicationDateTime &&
      comment == other.comment &&
      favorite == other.favorite &&
      repost == other.repost &&
      url == other.url
  }

  override fun hashCode(): Int {
    return Objects.hash(
      author,
      reposter,
      content,
      publicationDateTime,
      comment,
      favorite,
      repost,
      url
    )
  }

  override fun toString(): String {
    return "Repost(id=$id, author=$author, reposter=$reposter, content=$content, " +
      "publicationDateTime=$publicationDateTime, comment=$comment, favorite=$favorite," +
      "repost=$repost, url=$url)"
  }

  companion object
}

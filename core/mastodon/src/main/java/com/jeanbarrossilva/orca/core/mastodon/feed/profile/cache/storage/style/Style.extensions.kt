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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.style

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.std.styledstring.style.Style
import com.jeanbarrossilva.orca.std.styledstring.style.type.Bold
import com.jeanbarrossilva.orca.std.styledstring.style.type.Hashtag
import com.jeanbarrossilva.orca.std.styledstring.style.type.Italic
import com.jeanbarrossilva.orca.std.styledstring.style.type.Link
import com.jeanbarrossilva.orca.std.styledstring.style.type.Mention

/**
 * Name of this [Style].
 *
 * @throws IllegalStateException If no name has been defined for this type of [Style].
 */
internal val Style.name
  @Throws(IllegalArgumentException::class)
  get() =
    when (this) {
      is Bold -> "bold"
      is Hashtag -> "hashtag"
      is Italic -> "italic"
      is Mention -> "mention"
      is Link -> "link"
      else ->
        throw IllegalArgumentException("No name specified for a ${this::class.simpleName} style.")
    }

/**
 * Converts this [Style] into a [MastodonStyleEntity].
 *
 * @param postID ID of the [Post] to which this [Style] belongs.
 */
internal fun Style.toHttpStyleEntity(postID: String): MastodonStyleEntity {
  val url = if (this is Mention) url.toString() else null
  return MastodonStyleEntity(id = 0, postID, name, indices.first, indices.last, url)
}

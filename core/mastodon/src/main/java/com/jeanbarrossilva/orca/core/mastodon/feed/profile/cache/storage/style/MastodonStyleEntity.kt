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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import com.jeanbarrossilva.orca.std.styledstring.style.Style
import com.jeanbarrossilva.orca.std.styledstring.style.type.Bold
import com.jeanbarrossilva.orca.std.styledstring.style.type.Hashtag
import com.jeanbarrossilva.orca.std.styledstring.style.type.Italic
import com.jeanbarrossilva.orca.std.styledstring.style.type.Link
import com.jeanbarrossilva.orca.std.styledstring.style.type.Mention
import java.net.URL

/**
 * Primitive information to be stored about a [Style].
 *
 * @param id Automatically generated unique identifier.
 * @param parentID ID of the parent that owns the [StyledString] to which the [Style] belongs.
 * @param name Name of the [Style].
 * @param startIndex Indicates the first index in the [Post]'s [content][Post.content]'s
 *   [text][Content.text] to which the [Style] has been applied.
 * @param endIndex Final position in the [Post]'s [content][Post.content]'s [text][Content.text]
 *   that has the [Style].
 * @param url URL [String] to which the styled portion leads if it happens to be a [Link] or `null`
 *   if it isn't.
 * @see Style.name
 * @see Style.indices
 */
@Entity(tableName = "styles")
internal data class MastodonStyleEntity(
  @PrimaryKey(autoGenerate = true) val id: Long,
  @ColumnInfo(name = "parent_id") val parentID: String,
  @ColumnInfo(name = "name") val name: String,
  @ColumnInfo(name = "start_index") val startIndex: Int,
  @ColumnInfo(name = "end_index") val endIndex: Int,
  @ColumnInfo(name = "url") val url: String?
) {
  /**
   * Converts this [MastodonStyleEntity] into a [Style].
   *
   * @throws IllegalStateException If the [name] isn't that of an existing [Style].
   */
  @Throws(IllegalStateException::class)
  fun toStyle(): Style {
    val indices = startIndex..endIndex
    return when (name) {
      "bold" -> Bold(indices)
      "hashtag" -> Hashtag(indices)
      "italic" -> Italic(indices)
      "link" -> Link.to(URL(url), indices)
      "mention" -> Mention(indices, URL(url))
      else -> throw IllegalStateException("Unknown style name: \"$name\".")
    }
  }
}

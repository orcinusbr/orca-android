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

package br.com.orcinus.orca.core.mastodon.feed.profile.cache.storage.style

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.Style
import java.net.URI

/**
 * Primitive information to be stored about a [Style].
 *
 * @param id Automatically generated unique identifier.
 * @param parentID ID of the parent that owns the [Markdown] to which the [Style] belongs.
 * @param name Name of the [Style].
 * @param startIndex Indicates the first index in the [Post]'s [content][Post.content]'s
 *   [text][Content.text] to which the [Style] has been applied.
 * @param endIndex Final position in the [Post]'s [content][Post.content]'s [text][Content.text]
 *   that has the [Style].
 * @param uri URI [String] to which the styled portion leads if it happens to be a [Link] or `null`
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
  @ColumnInfo(name = "uri") val uri: String?
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
      "bold" -> Style.Bold(indices)
      "hashtag" -> Style.Hashtag(indices)
      "italic" -> Style.Italic(indices)
      "link" -> Style.Link.to(URI(uri), indices)
      "mention" -> Style.Mention(indices, URI(uri))
      else -> throw IllegalStateException("Unknown style name: \"$name\".")
    }
  }
}

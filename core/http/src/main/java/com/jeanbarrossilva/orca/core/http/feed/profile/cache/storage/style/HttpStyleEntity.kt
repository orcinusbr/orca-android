package com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage.style

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.std.styledstring.Style
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import com.jeanbarrossilva.orca.std.styledstring.type.Bold
import com.jeanbarrossilva.orca.std.styledstring.type.Hashtag
import com.jeanbarrossilva.orca.std.styledstring.type.Italic
import com.jeanbarrossilva.orca.std.styledstring.type.Link
import com.jeanbarrossilva.orca.std.styledstring.type.Mention
import java.net.URL

/**
 * Primitive information to be stored about a [Style].
 *
 * @param id Automatically generated unique identifier.
 * @param parentID ID of the parent that owns the [StyledString] to which the [Style] belongs.
 * @param name Name of the [Style].
 * @param startIndex Indicates the first index in the [Toot]'s [content][Toot.content]'s
 *   [text][Content.text] to which the [Style] has been applied.
 * @param endIndex Final position in the [Toot]'s [content][Toot.content]'s [text][Content.text]
 *   that has the [Style].
 * @param url URL [String] to which the styled portion leads if it happens to be a [Link] or `null`
 *   if it isn't.
 * @see Style.name
 * @see Style.indices
 */
@Entity(tableName = "styles")
internal data class HttpStyleEntity(
  @PrimaryKey(autoGenerate = true) val id: Long,
  @ColumnInfo(name = "parent_id") val parentID: String,
  @ColumnInfo(name = "name") val name: String,
  @ColumnInfo(name = "start_index") val startIndex: Int,
  @ColumnInfo(name = "end_index") val endIndex: Int,
  @ColumnInfo(name = "url") val url: String?
) {
  /**
   * Converts this [HttpStyleEntity] into a [Style].
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

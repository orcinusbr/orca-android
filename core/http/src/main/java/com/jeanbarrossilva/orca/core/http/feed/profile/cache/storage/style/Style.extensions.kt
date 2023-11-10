package com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage.style

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.std.styledstring.Style
import com.jeanbarrossilva.orca.std.styledstring.type.Bold
import com.jeanbarrossilva.orca.std.styledstring.type.Hashtag
import com.jeanbarrossilva.orca.std.styledstring.type.Italic
import com.jeanbarrossilva.orca.std.styledstring.type.Link
import com.jeanbarrossilva.orca.std.styledstring.type.Mention

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
 * Converts this [Style] into an [HttpStyleEntity].
 *
 * @param tootID ID of the [Toot] to which this [Style] belongs.
 */
internal fun Style.toHttpStyleEntity(tootID: String): HttpStyleEntity {
  val url = if (this is Mention) url.toString() else null
  return HttpStyleEntity(id = 0, tootID, name, indices.first, indices.last, url)
}

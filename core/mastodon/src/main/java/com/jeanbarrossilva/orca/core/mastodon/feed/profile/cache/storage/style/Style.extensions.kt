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

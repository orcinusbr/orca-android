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

import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.std.markdown.style.Style
import br.com.orcinus.orca.std.markdown.style.type.Bold
import br.com.orcinus.orca.std.markdown.style.type.Hashtag
import br.com.orcinus.orca.std.markdown.style.type.Italic
import br.com.orcinus.orca.std.markdown.style.type.Link
import br.com.orcinus.orca.std.markdown.style.type.Mention

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

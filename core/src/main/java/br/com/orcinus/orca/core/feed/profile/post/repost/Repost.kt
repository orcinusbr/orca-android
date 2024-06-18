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

package br.com.orcinus.orca.core.feed.profile.post.repost

import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.Post
import java.util.Objects

/** [Post] that has been reposted by someone else. */
abstract class Repost internal constructor() : Post {
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
      uri == other.uri
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
      uri
    )
  }

  override fun toString(): String {
    return "Repost(id=$id, author=$author, reposter=$reposter, content=$content, " +
      "publicationDateTime=$publicationDateTime, comment=$comment, favorite=$favorite," +
      "repost=$repost, uri=$uri)"
  }

  companion object
}

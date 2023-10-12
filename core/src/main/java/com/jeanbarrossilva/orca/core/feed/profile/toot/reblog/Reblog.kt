package com.jeanbarrossilva.orca.core.feed.profile.toot.reblog

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import java.util.Objects

/** [Toot] that has been reblogged by someone else. */
abstract class Reblog internal constructor() : Toot() {
  /** [Author] by which this [Reblog] has been created. */
  abstract val reblogger: Author

  override fun equals(other: Any?): Boolean {
    return other is Reblog &&
      id == other.id &&
      author == other.author &&
      reblogger == other.reblogger &&
      content == other.content &&
      publicationDateTime == other.publicationDateTime &&
      comment == other.comment &&
      favorite == other.favorite &&
      reblog == other.reblog &&
      url == other.url
  }

  override fun hashCode(): Int {
    return Objects.hash(
      author,
      reblogger,
      content,
      publicationDateTime,
      comment,
      favorite,
      reblog,
      url
    )
  }

  override fun toString(): String {
    return "Reblog(id=$id, author=$author, reblogger=$reblogger, content=$content, " +
      "publicationDateTime=$publicationDateTime, comment=$comment, favorite=$favorite," +
      "reblog=$reblog, url=$url)"
  }

  companion object
}

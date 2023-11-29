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

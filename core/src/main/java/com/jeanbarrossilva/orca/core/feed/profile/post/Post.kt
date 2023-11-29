package com.jeanbarrossilva.orca.core.feed.profile.post

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.Stat
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import java.io.Serializable
import java.net.URL
import java.time.ZonedDateTime

/** Content that's been posted by a user, the [author]. */
abstract class Post : Serializable {
  /** Unique identifier. */
  abstract val id: String

  /** [Author] that has authored this [Post]. */
  abstract val author: Author

  /** [Content] that's been composed by the [author]. */
  abstract val content: Content

  /** Zoned moment in time in which this [Post] was published. */
  abstract val publicationDateTime: ZonedDateTime

  /** [Stat] for comments. */
  abstract val comment: Stat<Post>

  /** [Stat] for favorites. */
  abstract val favorite: ToggleableStat<Profile>

  /** [Stat] for reposts. */
  abstract val repost: ToggleableStat<Profile>

  /** [URL] that leads to this [Post]. */
  abstract val url: URL

  companion object
}

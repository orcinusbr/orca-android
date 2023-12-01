/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

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

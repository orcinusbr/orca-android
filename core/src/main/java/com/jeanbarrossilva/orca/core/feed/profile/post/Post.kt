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

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.Actor
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

  /** Creates a [DeletablePost] from this [Post]. */
  abstract fun asDeletable(): DeletablePost

  /**
   * Creates a [DeletablePost] from this [Post] if its [author] is identified by the
   * [authenticated][Actor.Authenticated] [Actor]'s ID; otherwise, returns this one.
   *
   * @param authenticationLock [AuthenticationLock] by which the ID of the [Actor] will be provided
   *   and compared to the [author]'s.
   * @see Actor.Authenticated.id
   */
  internal suspend fun asDeletableOrThis(authenticationLock: SomeAuthenticationLock): Post {
    /**
     * TODO: Not require the actor to be authenticated in order to return either this post or a
     *   deletable one from it. May be troublesome when allowing unauthenticated ones to browse
     *   through the federated feed.
     */
    return authenticationLock.scheduleUnlock { if (it.id == author.id) asDeletable() else this }
  }

  companion object
}

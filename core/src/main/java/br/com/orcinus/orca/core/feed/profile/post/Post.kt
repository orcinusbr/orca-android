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

package br.com.orcinus.orca.core.feed.profile.post

import br.com.orcinus.orca.core.InternalCoreApi
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.stat.Stat
import br.com.orcinus.orca.core.feed.profile.post.stat.addable.AddableStat
import br.com.orcinus.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import java.net.URI
import java.time.ZonedDateTime

/** Content that's been posted by a user, the [author]. */
abstract class Post @InternalCoreApi constructor() {
  /**
   * [ActorProvider] for determining whether ownership can be given to the current [Actor].
   *
   * @see own
   */
  protected abstract val actorProvider: ActorProvider

  /** Unique identifier. */
  abstract val id: String

  /** [Author] that has authored this [Post]. */
  abstract val author: Author

  /** [Content] that's been composed by the [author]. */
  abstract val content: Content

  /** Zoned moment in time in which this [Post] was published. */
  abstract val publicationDateTime: ZonedDateTime

  /** [Stat] for comments. */
  abstract val comment: AddableStat<Post>

  /** [Stat] for favorites. */
  abstract val favorite: ToggleableStat<Profile>

  /** [Stat] for reposts. */
  abstract val repost: ToggleableStat<Profile>

  /** [URI] that leads to this [Post]. */
  abstract val uri: URI

  /**
   * Attempts to own this [Post], returning an equivalent [OwnedPost] if it does, in fact, belong to
   * the currently authenticated [Actor] by which ownership was requested. Otherwise, returns this
   * [Post].
   */
  suspend fun own(): Post {
    val actor = actorProvider.provide()
    return when (actor) {
      is Actor.Unauthenticated -> this
      is Actor.Authenticated -> toOwnedPost()
    }
  }

  /**
   * Obtains the [ActorProvider] for determining whether ownership can be given to the current
   * [Actor].
   *
   * @see own
   */
  internal fun getActorProvider(): ActorProvider {
    return actorProvider
  }

  /**
   * Converts this [Post] into an owned one. Called when ownership of it is requested and
   * subsequently conceived.
   *
   * @see own
   */
  protected abstract suspend fun toOwnedPost(): OwnedPost

  companion object
}

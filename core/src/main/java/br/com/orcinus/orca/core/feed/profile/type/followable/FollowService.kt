/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.core.feed.profile.type.followable

import br.com.orcinus.orca.core.InternalCoreApi
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.ProfileProvider
import br.com.orcinus.orca.std.func.monad.Maybe
import br.com.orcinus.orca.std.func.monad.flatMap
import kotlinx.coroutines.flow.first

/**
 * Service by which the [Follow] status of a [FollowableProfile] in relation to another is changed.
 */
abstract class FollowService @InternalCoreApi constructor() {
  /** [ProfileProvider] by which [FollowableProfile]s are provided. */
  protected abstract val profileProvider: ProfileProvider

  /**
   * [IllegalArgumentException] thrown when the [Follow] status of a [Profile] that does not exist
   * or is not followable is requested to be changed — by either toggling it or setting it to that
   * which succeeds it.
   */
  class NonFollowableProfileException @InternalCoreApi constructor(override val cause: Throwable?) :
    IllegalArgumentException(cause)

  /**
   * Toggles the [Follow] status of the currently authenticated [Actor] regarding the specified
   * [FollowableProfile].
   *
   * @param profileID ID of the [FollowableProfile] to follow, request to follow or unfollow.
   * @see Follow.toggled
   * @see FollowableProfile.follow
   * @see FollowableProfile.id
   */
  suspend fun toggle(profileID: String) =
    getProfile(profileID).flatMap { setFollow(it, it.follow.toggled()) }

  /**
   * Defines the [Follow] status of the currently authenticated [Actor] regarding the specified
   * [FollowableProfile] as that which follows the current one.
   *
   * Can fail with a [ProfileProvider.NonexistentProfileException], a
   * [NonFollowableProfileException] or any [Exception] resulted from attempting to change the
   * status.
   *
   * @param profileID ID of the [FollowableProfile].
   * @see FollowableProfile.follow
   * @see FollowableProfile.id
   * @see Follow.next
   */
  suspend fun next(profileID: String) =
    getProfile(profileID).flatMap { setFollow(it, it.follow.next()) }

  /**
   * Defines the [Follow] status of the currently authenticated [Actor] regarding the specified
   * [FollowableProfile] to the given one.
   *
   * @param T [Follow] status.
   * @param profile [FollowableProfile] whose [Follow] status is to be changed.
   * @param follow [Follow] status to be set.
   */
  protected abstract suspend fun <T : Follow> setFollow(
    profile: FollowableProfile<T>,
    follow: T
  ): Maybe<*, Unit>

  /**
   * Creates a variant-specific [NonFollowableProfileException].
   *
   * @param profileID ID of the [Profile] which cannot be followed.
   */
  protected abstract fun createNonFollowableProfileException(
    profileID: String
  ): NonFollowableProfileException

  /**
   * Obtains the [FollowableProfile] identified with the given ID.
   *
   * Can fail either with a [ProfileProvider.NonexistentProfileException] or a
   * [NonFollowableProfileException].
   *
   * @param profileID ID of the [FollowableProfile] to be obtained.
   * @see FollowableProfile.id
   */
  private suspend fun getProfile(profileID: String) =
    profileProvider.provide(profileID).flatMap { profileFlow ->
      @Suppress("UNCHECKED_CAST")
      (profileFlow.first() as? FollowableProfile<Follow>)?.let { followableProfile ->
        Maybe.successful(followableProfile)
      }
        ?: Maybe.failed(createNonFollowableProfileException(profileID))
    }
}

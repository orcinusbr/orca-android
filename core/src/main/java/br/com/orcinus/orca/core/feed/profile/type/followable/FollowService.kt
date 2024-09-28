/*
 * Copyright © 2024 Orcinus
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
import br.com.orcinus.orca.core.feed.profile.ProfileProvider
import kotlinx.coroutines.flow.first

/**
 * Service by which the [Follow] status of a [FollowableProfile] in relation to another is changed.
 */
abstract class FollowService @InternalCoreApi constructor() {
  /** [ProfileProvider] by which [FollowableProfile]s are provided. */
  protected abstract val profileProvider: ProfileProvider

  /**
   * Toggles the [Follow] status of the currently authenticated [Actor] regarding the specified
   * [FollowableProfile].
   *
   * @param profileID ID of the [FollowableProfile] to follow, request to follow or unfollow.
   * @throws IllegalArgumentException If the [profileID] is not that of a [FollowableProfile].
   * @see Follow.toggled
   * @see FollowableProfile.follow
   * @see FollowableProfile.id
   */
  @Throws(IllegalArgumentException::class)
  suspend fun toggle(profileID: String) {
    val profile = getProfile(profileID)
    setFollow(profile, profile.follow.toggled())
  }

  /**
   * Defines the [Follow] status of the currently authenticated [Actor] regarding the specified
   * [FollowableProfile] as that which follows the current one.
   *
   * @param profileID ID of the [FollowableProfile].
   * @throws IllegalArgumentException If the [profileID] is not that of a [FollowableProfile].
   * @see FollowableProfile.follow
   * @see FollowableProfile.id
   * @see Follow.next
   */
  @Throws(IllegalArgumentException::class)
  suspend fun next(profileID: String) {
    val profile = getProfile(profileID)
    setFollow(profile, profile.follow.next())
  }

  /**
   * Defines the [Follow] status of the currently authenticated [Actor] regarding the specified
   * [FollowableProfile] to the given one.
   *
   * @param T [Follow] status.
   * @param profile [FollowableProfile] whose [Follow] status is to be changed.
   * @param follow [Follow] status to be set.
   */
  protected abstract suspend fun <T : Follow> setFollow(profile: FollowableProfile<T>, follow: T)

  /**
   * Obtains the [FollowableProfile] identified with the given ID.
   *
   * @param profileID ID of the [FollowableProfile] to be obtained.
   * @throws IllegalArgumentException If the [profileID] is not that of a [FollowableProfile].
   * @see FollowableProfile.id
   */
  @Throws(IllegalArgumentException::class)
  private suspend fun getProfile(profileID: String): FollowableProfile<Follow> {
    @Suppress("UNCHECKED_CAST")
    return profileProvider.provide(profileID).first() as? FollowableProfile<Follow>
      ?: throw IllegalArgumentException("Profile identified as \"$profileID\" is not followable.")
  }
}

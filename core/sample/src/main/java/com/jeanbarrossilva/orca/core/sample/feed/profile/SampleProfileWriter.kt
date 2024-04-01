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

package com.jeanbarrossilva.orca.core.sample.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable.replacingOnceBy
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.followable.SampleFollowableProfile
import kotlinx.coroutines.flow.update

/** Performs [Profile]-related writing operations. */
class SampleProfileWriter internal constructor(private val provider: SampleProfileProvider) {
  /**
   * Inserts the given [profile], replacing the existing one that has the same [ID][Profile.id] if
   * it's there.
   *
   * @param profile [Profile] to be added.
   */
  fun insert(profile: Profile) {
    delete(profile.id)
    provider.profilesFlow.update { it + profile }
  }

  /** Resets this [SampleProfileProvider] to its default state. */
  fun reset() {
    provider.profilesFlow.value = provider.defaultProfiles
  }

  /**
   * Updates the [follow][SampleFollowableProfile.follow] status of the [Profile] whose
   * [ID][SampleFollowableProfile.id] is equal the given [id].
   *
   * @param id [Profile]'s [ID][SampleFollowableProfile.id].
   * @param follow [Follow] status to update the [SampleFollowableProfile.follow] to.
   */
  suspend fun <T : Follow> updateFollow(id: String, follow: T) {
    update(id) {
      @Suppress("UNCHECKED_CAST") (this as SampleFollowableProfile<T>).copy(follow = follow)
    }
  }

  /**
   * Deletes the [Profile] whose [ID][Profile.id] equals to the given one.
   *
   * @param id ID of the [Profile] to be deleted.
   */
  internal fun delete(id: String) {
    provider.profilesFlow.update { profiles ->
      profiles.toMutableList().apply { removeIf { profile -> profile.id == id } }.toList()
    }
  }

  /**
   * Replaces the currently existing [Profile] identified as [id] by its updated version.
   *
   * @param id ID of the [Profile] to be updated.
   * @param update Changes to be made to the existing [Profile].
   * @throws ProfileProvider.NonexistentProfileException If no [Profile] with such [ID][Profile.id]
   *   exists.
   */
  internal suspend fun update(id: String, update: Profile.() -> Profile) {
    if (provider.contains(id)) {
      provider.profilesFlow.update { profiles ->
        profiles.replacingOnceBy(update) { profile -> profile.id == id }
      }
    } else {
      throw ProfileProvider.NonexistentProfileException(id)
    }
  }
}

/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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

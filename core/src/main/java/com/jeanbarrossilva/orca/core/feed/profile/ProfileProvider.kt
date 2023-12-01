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

package com.jeanbarrossilva.orca.core.feed.profile

import kotlinx.coroutines.flow.Flow

/** Provides a [Profile] through [onProvide]. */
abstract class ProfileProvider {
  /**
   * [IllegalArgumentException] thrown when a [Profile] that doesn't exist is requested to be
   * provided.
   *
   * @param id ID of the [Profile] requested to be provided.
   */
  class NonexistentProfileException(id: String) :
    IllegalArgumentException("Profile identified as \"$id\" doesn't exist.")

  /**
   * Gets the [Profile] identified as [id].
   *
   * @param id ID of the [Profile] to be provided.
   * @throws NonexistentProfileException If no [Profile] with such [ID][Profile.id] exists.
   * @see Profile.id
   */
  suspend fun provide(id: String): Flow<Profile> {
    return if (contains(id)) onProvide(id) else throw NonexistentProfileException(id)
  }

  /**
   * Whether a [Profile] identified as [id] exists.
   *
   * @param id ID of the [Profile] whose existence will be checked.
   */
  protected abstract suspend fun contains(id: String): Boolean

  /**
   * Gets the [Profile] identified as [id].
   *
   * @param id ID of the [Profile] to be provided.
   * @see Profile.id
   */
  protected abstract suspend fun onProvide(id: String): Flow<Profile>
}

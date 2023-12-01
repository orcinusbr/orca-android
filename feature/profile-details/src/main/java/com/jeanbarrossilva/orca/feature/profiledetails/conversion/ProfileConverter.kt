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

package com.jeanbarrossilva.orca.feature.profiledetails.conversion

import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails

/** Converts a [Profile] into a [ProfileDetails] through [convert]. */
internal abstract class ProfileConverter {
  /** [ProfileConverter] to fallback to in case this one's [convert] returns `null`. */
  abstract val next: ProfileConverter?

  /**
   * Converts the given [profile] into [ProfileDetails].
   *
   * @param profile [Profile] to convert into [ProfileDetails].
   * @param colors [Colors] by which visuals can be colored.
   */
  fun convert(profile: Profile, colors: Colors): ProfileDetails? {
    return onConvert(profile, colors) ?: next?.convert(profile, colors)
  }

  /**
   * Converts the given [profile] into [ProfileDetails].
   *
   * Returning `null` signals that this [ProfileConverter] cannot perform the conversion and that
   * the operation should be delegated to the [next] one.
   *
   * @param profile [Profile] to convert into [ProfileDetails].
   * @param colors [Colors] by which visuals can be colored.
   */
  protected abstract fun onConvert(profile: Profile, colors: Colors): ProfileDetails?
}

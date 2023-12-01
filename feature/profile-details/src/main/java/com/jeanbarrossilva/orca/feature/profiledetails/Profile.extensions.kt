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

package com.jeanbarrossilva.orca.feature.profiledetails

import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.ProfileConverterFactory
import kotlinx.coroutines.CoroutineScope

/**
 * Converts this core [ProfileDetails] into [ProfileDetails].
 *
 * @param coroutineScope [CoroutineScope] through which converted [Profile]-related suspending will
 *   be performed.
 * @param colors [Colors] by which visuals can be colored.
 */
internal fun Profile.toProfileDetails(
  coroutineScope: CoroutineScope,
  colors: Colors
): ProfileDetails {
  val details = ProfileConverterFactory.create(coroutineScope).convert(this, colors)
  return requireNotNull(details)
}

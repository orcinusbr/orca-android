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

package com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter.followable

import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails

/** Converts this [Follow] into a [ProfileDetails.Followable.Status]. */
internal fun Follow.toStatus(): ProfileDetails.Followable.Status {
  return when (this) {
    Follow.Public.following(),
    Follow.Private.following() -> ProfileDetails.Followable.Status.FOLLOWING
    Follow.Private.requested() -> ProfileDetails.Followable.Status.REQUESTED
    else -> ProfileDetails.Followable.Status.UNFOLLOWED
  }
}

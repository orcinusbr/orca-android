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

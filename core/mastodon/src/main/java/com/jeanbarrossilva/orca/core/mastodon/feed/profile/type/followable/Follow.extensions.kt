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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.type.followable

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow

/**
 * Gets the route to which a call would equate to this [Follow]'s [toggled][Follow.toggled] state.
 *
 * @param profile [Profile] to which this [Follow] is related.
 */
internal fun Follow.getToggledRoute(profile: Profile): String {
  return when (this) {
    Follow.Public.following(),
    Follow.Private.requested(),
    Follow.Private.following() -> "/api/v1/accounts/${profile.id}/unfollow"
    else -> "/api/v1/accounts/${profile.id}/follow"
  }
}

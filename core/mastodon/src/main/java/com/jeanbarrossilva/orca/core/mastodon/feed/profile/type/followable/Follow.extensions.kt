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

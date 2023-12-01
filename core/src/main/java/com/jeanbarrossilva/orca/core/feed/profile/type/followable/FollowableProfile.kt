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

package com.jeanbarrossilva.orca.core.feed.profile.type.followable

import com.jeanbarrossilva.orca.core.feed.profile.Profile

/** [Profile] whose [follow] status can be toggled. */
abstract class FollowableProfile<T : Follow> : Profile {
  /** Current [Follow] status. */
  abstract val follow: T

  /** Toggles the [follow] status. */
  suspend fun toggleFollow() {
    val toggledFollow = follow.toggled()
    val matchingToggledFollow = Follow.requireVisibilityMatch(follow, toggledFollow)
    onChangeFollowTo(matchingToggledFollow)
  }

  /**
   * Callback run whenever the [Follow] status is changed to [follow].
   *
   * @param follow Changed [Follow] status.
   */
  protected abstract suspend fun onChangeFollowTo(follow: T)

  companion object
}

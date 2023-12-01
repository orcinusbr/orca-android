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

package com.jeanbarrossilva.orca.platform.ui.component.timeline.refresh

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline

/**
 * Swipe-to-refresh behavior configuration for a [Timeline].
 *
 * @param isInProgress Whether the [Timeline] is currently being refreshed.
 * @param indicatorOffset Amount of [Dp]s by which the refresh indicator should be offset in the
 *   y-axis.
 * @param listener [Listener] to be notified of refreshes.
 */
@Immutable
data class Refresh(val isInProgress: Boolean, val indicatorOffset: Dp, val listener: Listener) {
  /**
   * Listens to refreshes.
   *
   * @see onRefresh
   */
  fun interface Listener {
    /** Callback run whenever the [Timeline] is refreshed. */
    fun onRefresh()
  }

  init {
    require(indicatorOffset.isSpecified) {
      "Cannot offset the refresh indicator by an unspecified amount of DPs."
    }
  }

  companion object {
    /** Never-active, no-op [Refresh]. */
    internal val Disabled = Refresh(isInProgress = false, indicatorOffset = 0.dp) {}

    /**
     * [Refresh] that remains active indefinitely.
     *
     * @see isInProgress
     */
    val Indefinite = Refresh(isInProgress = true, indicatorOffset = 0.dp) {}
  }
}

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

package com.jeanbarrossilva.orca.platform.autos.reactivity

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

/**
 * [NestedScrollConnection] that notifies the [listener] of scroll changes.
 *
 * @param listener [OnBottomAreaAvailabilityChangeListener] to be notified.
 */
class BottomAreaAvailabilityNestedScrollConnection
internal constructor(private val listener: OnBottomAreaAvailabilityChangeListener) :
  NestedScrollConnection {
  override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
    val currentOffsetY = listener.getCurrentOffsetY()
    val heightAsFloat = listener.height.toFloat()
    val changedOffsetY = (currentOffsetY - available.y).coerceIn(0f, heightAsFloat)
    listener.onBottomAreaAvailabilityChange(changedOffsetY)
    return Offset.Zero
  }

  companion object {
    /**
     * [BottomAreaAvailabilityNestedScrollConnection] with an empty
     * [OnBottomAreaAvailabilityChangeListener].
     *
     * @see OnBottomAreaAvailabilityChangeListener.empty
     */
    val empty =
      BottomAreaAvailabilityNestedScrollConnection(OnBottomAreaAvailabilityChangeListener.empty)
  }
}

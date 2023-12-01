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

/** Listens to changes on the availability of the utmost bottom portion of the displayed content. */
interface OnBottomAreaAvailabilityChangeListener {
  /** Provides the height of the UI component in the bottom area. */
  val height: Int

  /** Provides the current offset in the Y-axis of the UI component in the bottom area. */
  fun getCurrentOffsetY(): Float

  /**
   * Callback run whenever the availability of the bottom area is changed.
   *
   * @param offsetY Amount of offset in the Y-axis to be applied to a UI component in the bottom
   *   area.
   */
  fun onBottomAreaAvailabilityChange(offsetY: Float)

  companion object {
    /** No-op [OnBottomAreaAvailabilityChangeListener]. */
    val empty =
      object : OnBottomAreaAvailabilityChangeListener {
        override val height = 0

        override fun getCurrentOffsetY(): Float {
          return 0f
        }

        override fun onBottomAreaAvailabilityChange(offsetY: Float) {}
      }
  }
}

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

package com.jeanbarrossilva.orca.platform.ui.core.navigation.transition

import androidx.fragment.app.FragmentTransaction

/**
 * Indicates how navigation from one destination to another will transition.
 *
 * @param value [FragmentTransaction]-related value that's equivalent to this [Transition].
 */
@JvmInline
value class Transition internal constructor(internal val value: Int) {
  init {
    require(
      value == FragmentTransaction.TRANSIT_NONE ||
        value == FragmentTransaction.TRANSIT_FRAGMENT_FADE ||
        value == FragmentTransaction.TRANSIT_FRAGMENT_OPEN ||
        value == FragmentTransaction.TRANSIT_FRAGMENT_CLOSE
    ) {
      "Unknown value: $value."
    }
  }
}

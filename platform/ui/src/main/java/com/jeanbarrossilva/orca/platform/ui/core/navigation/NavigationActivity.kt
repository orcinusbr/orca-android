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

package com.jeanbarrossilva.orca.platform.ui.core.navigation

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import com.jeanbarrossilva.orca.platform.ui.core.content

/** [FragmentActivity] through which [Navigator]-based navigation can be performed. */
open class NavigationActivity : FragmentActivity() {
  /**
   * [Navigator] through which navigation can be performed.
   *
   * **NOTE**: Because the [FragmentContainerView] that this [NavigationActivity] holds needs to
   * have an ID for the [Navigator] to work properly, one is automatically generated and assigned to
   * it if it doesn't already have one.
   *
   * @throws IllegalStateException If a [FragmentContainerView] is not found within the [View] tree.
   */
  val navigator
    get() =
      content.get<FragmentContainerView>(isInclusive = false).also(View::identify).let {
        Navigator(supportFragmentManager, it.id)
      }
}

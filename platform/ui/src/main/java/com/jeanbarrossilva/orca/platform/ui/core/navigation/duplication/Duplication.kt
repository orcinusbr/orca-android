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

package com.jeanbarrossilva.orca.platform.ui.core.navigation.duplication

import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator

/** Indicates the approval or lack thereof of duplicate navigation. */
sealed class Duplication {
  /** Indicates that duplicate navigation is disallowed. */
  internal object Disallowed : Duplication() {
    override fun canNavigate(previousRoute: String?, currentRoute: String): Boolean {
      return previousRoute == null || currentRoute != previousRoute
    }
  }

  /** Indicates that duplicate navigation is allowed. */
  internal object Allowed : Duplication() {
    override fun canNavigate(previousRoute: String?, currentRoute: String): Boolean {
      return true
    }
  }

  /**
   * Determines whether navigation can be performed.
   *
   * @param previousRoute Route of the previous [Navigator.Navigation.Destination].
   * @param currentRoute Route to which navigation has been requested.
   */
  internal abstract fun canNavigate(previousRoute: String?, currentRoute: String): Boolean
}

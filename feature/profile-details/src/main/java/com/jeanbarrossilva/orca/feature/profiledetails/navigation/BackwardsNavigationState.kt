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

package com.jeanbarrossilva.orca.feature.profiledetails.navigation

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.feature.profiledetails.navigation.BackwardsNavigationState.Available
import com.jeanbarrossilva.orca.feature.profiledetails.navigation.BackwardsNavigationState.Unavailable
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import java.io.Serializable

/**
 * Defines the availability of backwards navigation.
 *
 * @see Unavailable
 * @see Available
 */
sealed class BackwardsNavigationState : Serializable {
  /** Defines that backwards navigation is not available. */
  object Unavailable : BackwardsNavigationState() {
    @Composable override fun NavigationButton(modifier: Modifier) {}
  }

  /**
   * Defines that backwards navigation is available, and thus can be performed through
   * [navigateBackwards].
   */
  abstract class Available private constructor() : BackwardsNavigationState() {
    @Composable
    override fun NavigationButton(modifier: Modifier) {
      HoverableIconButton(onClick = ::navigateBackwards) {
        Icon(AutosTheme.iconography.back.asImageVector, contentDescription = "Back")
      }
    }

    /** Navigates backwards, to the previous screen. */
    internal abstract fun navigateBackwards()

    companion object {
      /**
       * Creates an [Available] instance.
       *
       * @param onBackwardsNavigation Action to be performed when [navigateBackwards] is called.
       */
      fun createInstance(onBackwardsNavigation: () -> Unit): Available {
        return object : Available() {
          override fun navigateBackwards() {
            onBackwardsNavigation()
          }
        }
      }
    }
  }

  /**
   * [Composable] that represents the action that can be performed.
   *
   * @param modifier [Modifier] to be applied to the underlying [Composable].
   */
  @Composable internal abstract fun NavigationButton(modifier: Modifier)
}

/** [Composable] that represents the action that can be performed. */
@Composable
internal fun BackwardsNavigationState.NavigationButton() {
  NavigationButton(Modifier)
}

/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
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

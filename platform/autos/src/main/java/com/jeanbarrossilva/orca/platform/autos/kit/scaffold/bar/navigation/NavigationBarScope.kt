/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.navigation

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.icon.HoverableIconButton

/**
 * Scope from which tabs can be added to a [NavigationBar].
 *
 * @see tab
 */
class NavigationBarScope internal constructor() {
  /** Index of the currently selected tab. */
  private var selectedTabIndex by mutableIntStateOf(0)

  /** Tabs that have been added. */
  internal val tabs = mutableStateListOf<@Composable () -> Unit>()

  /**
   * Adds a selectable navigation tab.
   *
   * @param onClick Action performed when the tab is clicked.
   * @param modifier [Modifier] to be applied to the underlying [HoverableIconButton].
   * @param content [Icon] to be shown.
   */
  fun tab(onClick: () -> Unit, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    tabs.add { HoverableIconButton(onClick, modifier.testTag(TAB_TAG), content) }
  }

  companion object {
    /** Tag by which a tab is identified for testing purposes. */
    const val TAB_TAG = "tab"
  }
}

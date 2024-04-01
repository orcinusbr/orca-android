/*
 * Copyright © 2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.platform.autos.test.kit.scaffold.bar.navigation

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.navigation.NavigationBar
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.navigation.NavigationBarScope

/** [SemanticsNodeInteraction] of a [NavigationBar]. */
fun SemanticsNodeInteractionsProvider.onNavigationBar(): SemanticsNodeInteraction {
  return onNode(isNavigationBar())
}

/**
 * [SemanticsNodeInteraction] of a [NavigationBar]'s tab.
 *
 * @see isNavigationBar
 * @see SemanticsNodeInteractionsProvider.onNavigationBar
 * @see NavigationBarScope.tab
 * @see isTab
 */
fun SemanticsNodeInteractionsProvider.onTab(): SemanticsNodeInteraction {
  return onNode(isTab())
}

/**
 * [SemanticsNodeInteractionCollection] of a [NavigationBar]'s tabs.
 *
 * @see isNavigationBar
 * @see SemanticsNodeInteractionsProvider.onNavigationBar
 * @see NavigationBarScope.tab
 * @see isTab
 */
fun SemanticsNodeInteractionsProvider.onTabs(): SemanticsNodeInteractionCollection {
  return onAllNodes(isTab())
}

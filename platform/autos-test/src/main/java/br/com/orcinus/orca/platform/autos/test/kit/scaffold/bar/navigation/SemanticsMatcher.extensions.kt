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

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasTestTag
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.navigation.NAVIGATION_BAR_TAG
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.navigation.NavigationBar
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.navigation.NavigationBarScope

/** [SemanticsMatcher] that matches a [NavigationBar]. */
fun isNavigationBar(): SemanticsMatcher {
  return hasTestTag(NAVIGATION_BAR_TAG)
}

/**
 * [SemanticsMatcher] that matches a tab of a [NavigationBar].
 *
 * @see isNavigationBar
 */
fun isTab(): SemanticsMatcher {
  return hasTestTag(NavigationBarScope.TAB_TAG)
}

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

package br.com.orcinus.orca.platform.autos.kit.scaffold.scope

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

/** Scope from which the [Content] of a [Scaffold] can be defined. */
class ScaffoldScope internal constructor() {
  /**
   * Creates a [Content] that is displayed all by itself.
   *
   * @param value [Composable] to be shown.
   */
  fun expanded(value: @Composable () -> Unit): Content {
    return Content.Expanded(value)
  }

  /**
   * Creates a [Content] that is shown as a result of the selection of a [NavigationBar] tab.
   *
   * @param value [Composable] to be shown.
   * @see NavigationBarScope.tab
   */
  fun navigable(value: @Composable () -> Unit): Content {
    return Content.Navigable(value)
  }
}

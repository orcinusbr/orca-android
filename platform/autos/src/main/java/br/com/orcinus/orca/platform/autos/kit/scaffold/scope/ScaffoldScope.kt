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

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.bottom.OrcaBottomNavigationView

/** Scope from which the [Content] of a [Scaffold] can be defined. */
@Deprecated(
  "Masking applied to the content, that was previously defined by whether it was \"expanded\" " +
    "or \"navigable\", is now determined by the view by which the scaffold is shown; thus, " +
    "specifying one of the two is redundant and a no-op."
)
class ScaffoldScope internal constructor() {
  /**
   * Creates a [Content] that is displayed all by itself.
   *
   * @param modifier [Modifier] to be applied to the underlying [Box].
   * @param value [Composable] to be shown.
   */
  fun expanded(modifier: Modifier = Modifier, value: @Composable () -> Unit): Content {
    return Content.Expanded(modifier, value)
  }

  /**
   * Creates a [Content] that is shown as a result of the selection of an [OrcaBottomNavigationView]
   * tab.
   *
   * @param modifier [Modifier] to be applied to the underlying [Box].
   * @param value [Composable] to be shown.
   */
  fun navigable(modifier: Modifier = Modifier, value: @Composable () -> Unit): Content {
    return Content.Navigable(modifier, value)
  }
}

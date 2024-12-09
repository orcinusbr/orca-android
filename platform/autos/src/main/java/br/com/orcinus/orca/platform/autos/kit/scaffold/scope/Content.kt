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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Defines how a [Composable] containing a specific context should be laid out. */
@Deprecated(
  "Masking applied to the content, that was previously defined by whether it was \"expanded\" " +
    "or \"navigable\", is now determined by the view by which the scaffold is shown; thus, " +
    "specifying one of the two is redundant and a no-op."
)
abstract class Content internal constructor() {
  /** [Modifier] to be applied to the underlying [Box]. */
  protected abstract val modifier: Modifier

  /** [Composable] to be shown. */
  protected abstract val value: @Composable () -> Unit

  /** [Content] that is displayed all by itself. */
  internal class Expanded(
    override val modifier: Modifier,
    override val value: @Composable () -> Unit
  ) : Content()

  /** [Content] that is shown as a result of the selection of a tab. */
  internal class Navigable(
    override val modifier: Modifier,
    override val value: @Composable () -> Unit
  ) : Content()

  /** [value] with the [modifier] applied to it. */
  @Composable
  internal fun Value() {
    Box(modifier) { value() }
  }
}

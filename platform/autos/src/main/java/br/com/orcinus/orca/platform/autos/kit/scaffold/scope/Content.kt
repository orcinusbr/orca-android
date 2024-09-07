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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.platform.autos.colors.LocalContainerColor
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.forms.asShape
import br.com.orcinus.orca.platform.autos.kit.bottom
import br.com.orcinus.orca.platform.autos.theme.AutosTheme

/** Defines how a [Composable] containing a specific context should be laid out. */
abstract class Content internal constructor() {
  /** [Modifier] to be applied to the underlying [Box]. */
  protected abstract val modifier: Modifier

  /** [Shape] by which the [Composable] is be shaped. */
  @get:Composable protected abstract val shape: Shape

  /** [Composable] to be shown. */
  internal abstract val value: @Composable () -> Unit

  /** [Content] that is displayed all by itself. */
  internal class Expanded(
    override val modifier: Modifier,
    override val value: @Composable () -> Unit
  ) : Content() {
    override val shape
      @Composable get() = RectangleShape
  }

  /** [Content] that is shown as a result of the selection of a tab. */
  internal class Navigable(
    override val modifier: Modifier,
    override val value: @Composable () -> Unit
  ) : Content() {
    override val shape
      @Composable get() = AutosTheme.forms.medium.asShape.bottom
  }

  /**
   * [value] clipped by the specified [shape].
   *
   * @param padding [PaddingValues] to be passed into the [value].
   */
  @Composable
  internal fun ClippedValue(padding: PaddingValues) {
    Box(
      Modifier.background(Colors.LIGHT.primary.container.asColor)
        .then(modifier)
        .padding(padding)
        .background(LocalContainerColor.current)
        .fillMaxSize()
    ) {
      value()
    }
  }
}

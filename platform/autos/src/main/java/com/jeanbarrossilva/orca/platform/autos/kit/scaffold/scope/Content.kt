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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.scope

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.jeanbarrossilva.orca.platform.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.autos.kit.bottom
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.navigation.NavigationBar
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.navigation.NavigationBarScope
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme

/** Defines how a [Composable] containing a specific context should be laid out. */
abstract class Content internal constructor() {
  /** [Shape] by which the [Composable] is be shaped. */
  @get:Composable protected abstract val shape: Shape

  /** [Composable] to be shown. */
  internal abstract val value: @Composable (padding: PaddingValues) -> Unit

  /** [Content] that is displayed all by itself. */
  internal class Expanded(override val value: @Composable (padding: PaddingValues) -> Unit) :
    Content() {
    override val shape
      @Composable get() = RectangleShape
  }

  /**
   * [Content] that is shown as a result of the selection of a [NavigationBar] tab.
   *
   * @see NavigationBarScope.tab
   */
  internal class Navigable(override val value: @Composable (padding: PaddingValues) -> Unit) :
    Content() {
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
    Box(Modifier.clip(shape)) { value(padding) }
  }
}

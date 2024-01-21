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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild

/** Dictates the way a [ButtonBar]'s container might be colored. */
sealed class ButtonBarMaterial {
  /** Indicates that the [Color] should be rendered as is. */
  data object Opaque : ButtonBarMaterial() {
    @Composable
    override fun Container(
      modifier: Modifier,
      color: Color,
      content: @Composable () -> Unit,
      measurePolicy: MeasurePolicy
    ) {
      Layout(content, Modifier.drawBehind { drawRect(color) }.then(modifier), measurePolicy)
    }
  }

  /**
   * Indicates that the [ButtonBar]'s container should be blurred.
   *
   * @param hazeState [HazeState] with which the vibrancy effect will be applied.
   */
  data class Vibrant(val hazeState: HazeState) : ButtonBarMaterial() {
    @Composable
    override fun Container(
      modifier: Modifier,
      color: Color,
      content: @Composable () -> Unit,
      measurePolicy: MeasurePolicy
    ) {
      Layout(
        content,
        Modifier.alpha(.8f).hazeChild(hazeState, tint = color.copy(alpha = .5f)).then(modifier),
        measurePolicy
      )
    }
  }

  /**
   * Container for a [ButtonBar], styled according to this [ButtonBarMaterial].
   *
   * @param modifier [Modifier] to be applied to the underlying [Layout].
   * @param color [Color] by which it is colored.
   * @param measurePolicy [MeasurePolicy] defining measurement and positioning within the [Layout].
   * @param content [Composable] placed into it.
   */
  @Composable
  internal abstract fun Container(
    modifier: Modifier,
    color: Color,
    content: @Composable () -> Unit,
    measurePolicy: MeasurePolicy
  )
}

/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.text.size

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText

/**
 * Provides [fontSizeInPx], that changes according to the last sizing operation that has been
 * performed.
 *
 * @param density [Density] through which the step size will be converted from [SP][TextUnitType.Sp]
 *   to pixels.
 * @param range [AutoSizeRange] within which the [AutoSizeText] should be sized.
 * @see autoSize
 */
internal class AutoSizer(private val density: Density, range: AutoSizeRange) {
  /** Minimum size in pixels. */
  private val minSizeInPx = range.start

  /** Current size in pixels. */
  private var fontSizeInPx by mutableFloatStateOf(range.endInclusive)

  /** Current size based on [fontSizeInPx], converted into [SP][TextUnitType.Sp]. */
  val size by derivedStateOf { with(density) { fontSizeInPx.toSp() } }

  /**
   * Changes the [size] based on the [result] to one that minimally fits into the layout bounds.
   *
   * @param result [TextLayoutResult] obtained by laying out the text.
   * @param canBeDrawn Whether the text can be drawn.
   * @param onFinish Callback called when it's finished sizing.
   */
  fun autoSize(result: TextLayoutResult, canBeDrawn: Boolean, onFinish: () -> Unit) {
    if (result.didOverflowHeight && !canBeDrawn) {
      decreaseSize(onFinish)
    } else {
      onFinish()
    }
  }

  /**
   * Decreases the [size] by 1 [SP][TextUnitType.Sp] until it reaches its minimum value.
   *
   * @param onFinish Callback called when it's finished sizing.
   * @see minSizeInPx
   */
  private fun decreaseSize(onFinish: () -> Unit) {
    val stepInPx = with(density) { 1.sp.toPx() }
    val nextFontSizeInPx = fontSizeInPx - stepInPx
    val hasReachedMinSize = nextFontSizeInPx <= minSizeInPx
    if (hasReachedMinSize) {
      fontSizeInPx = minSizeInPx
      onFinish()
    } else {
      fontSizeInPx = nextFontSizeInPx
    }
  }
}

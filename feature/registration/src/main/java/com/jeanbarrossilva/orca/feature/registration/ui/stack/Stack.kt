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

package com.jeanbarrossilva.orca.feature.registration.ui.stack

import androidx.annotation.FloatRange
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import com.jeanbarrossilva.orca.feature.registration.ui.stack.state.StackState
import com.jeanbarrossilva.orca.feature.registration.ui.stack.state.rememberStackState
import com.jeanbarrossilva.orca.feature.registration.ui.status.Status
import com.jeanbarrossilva.orca.feature.registration.ui.status.StatusCard
import com.jeanbarrossilva.orca.feature.registration.ui.status.rememberStatusCardState
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Maximum amount of items that are visible within a [Stack], including both foreground and
 * background ones.
 */
private const val MAX_VISIBLE_ITEM_COUNT = 3

/** Maximum amount of background items that are visible. */
private const val MAX_VISIBLE_BACKGROUND_ITEM_COUNT = MAX_VISIBLE_ITEM_COUNT - 1

/** Fraction by which the first background item is scaled. */
@FloatRange(from = 0.0, to = 1.0) private const val FIRST_BACKGROUND_ITEM_SCALE = .95f

/**
 * Fraction in the Y axis of the first background item that is visible. Also used as a basis for
 * calculating the fraction of all subsequent background items.
 *
 * @see calculateYOffsetForBackgroundItem
 */
@FloatRange(from = 0.0, to = 1.0) private const val FIRST_BACKGROUND_ITEM_VISIBILITY_FRACTION = .1f

/**
 * Stacks each of its added items on top of each other, with the one that has been added the most
 * recently being the totally visible one (resembling the behavior of an actual [java.util.Stack]).
 *
 * Realistically, only a certain amount of items will be rendered: the one added lastly (referred to
 * as the "foreground" one) and some of those previous to it (the "background" ones). The exact
 * quantity is determined by [MAX_VISIBLE_ITEM_COUNT].
 *
 * @param modifier [Modifier] that is applied to the underlying [Layout].
 * @param state [StackState] to which items can be added. Also the one provided to the [content].
 * @param content Configures the items to be shown and that can be added through the receiver
 *   [StackState].
 */
@Composable
internal fun Stack(
  modifier: Modifier = Modifier,
  state: StackState = rememberStackState(),
  content: StackState.() -> Unit
) {
  DisposableEffect(state, content) {
    state.content()
    onDispose {}
  }

  Layout({ state.contents.forEach { it() } }, modifier) { measurables, constraints ->
    val items = measurables.takeLast(MAX_VISIBLE_ITEM_COUNT).map { it.measure(constraints) }
    val foreground = items.lastOrNull() ?: return@Layout layout(0, 0) {}
    val background = items.take(MAX_VISIBLE_BACKGROUND_ITEM_COUNT)
    val backgroundYOffsets =
      background
        .mapIndexed { index, item -> IndexedValue(index.inc(), item) }
        .associate { (index, item) ->
          index to calculateYOffsetForBackgroundItem(index, item.height)
        }
    val backgroundHeight =
      backgroundYOffsets.values.withIndex().sumOf { (index, yOffset) ->
        scaleYOffsetForBackgroundItem(index, yOffset)
      }
    layout(foreground.width, foreground.height + backgroundHeight) {
      with(items) {
        forEachIndexed { index, item ->
          item.placeWithLayer(
            x = calculateCenteringXForItem(foreground.width, item.width),
            y = take(index).fold(0) { itemY, _ -> itemY - (backgroundYOffsets[index] ?: 0) },
            zIndex = index.toFloat() - items.lastIndex.toFloat()
          ) {
            val isBackgroundItem = index < items.lastIndex
            if (isBackgroundItem) {
              scaleX = calculateScaleForBackgroundItem(index)
              scaleY = scaleX
            }
          }
        }
      }
    }
  }
}

/**
 * Scales the [verticalOffset] of a background item according to the index at which it is.
 *
 * @param index Index at which the background item whose [verticalOffset] will be scaled is.
 * @param verticalOffset Amount to be scaled based on the [FIRST_BACKGROUND_ITEM_SCALE], by which
 *   the background item has been offset in the Y axis.
 */
private fun scaleYOffsetForBackgroundItem(index: Int, verticalOffset: Int): Int {
  val absVerticalOffset = abs(verticalOffset)
  return absVerticalOffset - (FIRST_BACKGROUND_ITEM_SCALE * absVerticalOffset).roundToInt() * index
}

/**
 * Calculates the offset in the Y axis of a background item.
 *
 * @param index Index at which the background item to be placed is.
 * @param height Height of the background item, as seen by the parent layout.
 */
private fun calculateYOffsetForBackgroundItem(index: Int, height: Int): Int {
  return -(FIRST_BACKGROUND_ITEM_VISIBILITY_FRACTION * index.inc() * height).roundToInt()
}

/**
 * Calculates the scale for a background item.
 *
 * @param index Index at which the background item to be scaled is.
 */
private fun calculateScaleForBackgroundItem(index: Int): Float {
  return FIRST_BACKGROUND_ITEM_SCALE / index.inc()
}

/**
 * Calculates a coordinate in the X axis for an item to be horizontally centered within its parent.
 *
 * @param parentWidth Width of the parent layout in which the child will be placed.
 * @param childWidth Width of the child layout to be placed.
 */
private fun calculateCenteringXForItem(parentWidth: Int, childWidth: Int): Int {
  return (parentWidth - childWidth) / 2
}

/** Preview of a [Stack]. */
@Composable
@MultiThemePreview
private fun StackPreview() {
  AutosTheme {
    Stack {
      item { StatusCard(rememberStatusCardState(Status.Failed)) { Text("Card #1") } }
      item { StatusCard(rememberStatusCardState(Status.Succeeded)) { Text("Card #2") } }
    }
  }
}

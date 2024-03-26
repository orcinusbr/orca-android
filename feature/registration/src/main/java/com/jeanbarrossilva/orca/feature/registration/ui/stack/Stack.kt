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
import androidx.annotation.IntRange
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.util.fastForEachIndexed
import com.jeanbarrossilva.orca.feature.registration.ui.stack.state.StackState
import com.jeanbarrossilva.orca.feature.registration.ui.stack.state.rememberStackState
import com.jeanbarrossilva.orca.feature.registration.ui.status.Status
import com.jeanbarrossilva.orca.feature.registration.ui.status.StatusCard
import com.jeanbarrossilva.orca.feature.registration.ui.status.rememberStatusCardState
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Maximum amount of items that are visible within a [Stack], including both foreground and
 * background ones.
 */
private const val MaxVisibleItemCount = 3

/** Maximum amount of background items that are visible. */
private const val MaxVisibleBackgroundItemCount = MaxVisibleItemCount - 1

/**
 * Amount to be subtracted from the quantity of total items that have been added onto a [Stack] for
 * the index of the first visible background one to be determined.
 */
private const val FurthermostVisibleBackgroundItemIndexSubtrahend =
  MaxVisibleItemCount - MaxVisibleBackgroundItemCount

/**
 * Fraction by which the first background item is scaled, based on which following ones will also be
 * scaled.
 */
@UnitFraction private const val InitialBackgroundItemScale = .95f

/**
 * Fraction in the Y axis by which the furthermost background item is offset. Also used as a basis
 * for calculating the Y offset of all subsequent background items.
 *
 * @see calculateUnscaledYOffsetForItem
 * @see calculateScaledYOffsetForBackgroundItem
 */
@UnitFraction private const val InitialBackgroundItemYOffsetFraction = .1f

/**
 * Denotes that a [Float] represents a fraction whose numerator is always 1 and whose denominator is
 * positive, consequently with a value that is within the `0f..1f` range.
 */
@FloatRange(from = 0.0, to = 1.0)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
private annotation class UnitFraction

/**
 * Denotes that an [Int] is the index of a background item.
 *
 * For an explanation on what foreground and background items are, refer both to [Stack]'s and
 * [requireBackgroundItemIndex]'s documentations.
 */
@IntRange(from = 0, to = MaxVisibleBackgroundItemCount.toLong())
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.VALUE_PARAMETER)
private annotation class BackgroundItemIndex

/**
 * Stacks each of its added items on top of each other, with the one that has been added the most
 * recently being the totally visible one (resembling the behavior of an actual [java.util.Stack]).
 *
 * Realistically, only a certain amount of items will be rendered: the one added lastly (referred to
 * as the "foreground" one) and some of those previous to it (the "background" ones). The exact
 * quantity is determined by [MaxVisibleItemCount].
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
    val items = measurables.takeLast(MaxVisibleItemCount).map { it.measure(constraints) }
    val foreground = items.lastOrNull() ?: return@Layout layout(0, 0) {}
    val foregroundIndex = items.lastIndex
    val foregroundWidth = foreground.width
    val foregroundHeight = foreground.height
    val foregroundYOffset = calculateUnscaledYOffsetForItem(foregroundIndex, foregroundHeight)
    val backgroundCount = items.size - FurthermostVisibleBackgroundItemIndexSubtrahend
    val background = items.take(backgroundCount)
    val backgroundScales =
      List(backgroundCount) { calculateScaleForBackgroundItem(backgroundCount, index = it) }
    val backgroundScaledYOffsets =
      background.mapIndexed { index, item ->
        calculateScaledYOffsetForBackgroundItem(
          backgroundCount,
          index,
          item.height,
          backgroundScales[index]
        )
      }
    val backgroundHeight = backgroundScaledYOffsets.sum()
    layout(foregroundWidth, backgroundHeight + foregroundHeight + foregroundYOffset) {
      background.fastForEachIndexed { index, backgroundItem ->
        backgroundItem.placeWithLayer(
          x = calculateCenteringXForBackgroundItem(foregroundWidth, backgroundItem.width),
          y = backgroundScaledYOffsets[index] - foregroundYOffset
        ) {
          scaleX = backgroundScales[index]
          scaleY = scaleX
        }
      }
      foreground.place(x = 0, y = backgroundHeight + foregroundYOffset)
    }
  }
}

/**
 * Calculates the scaled Y offset of a background item according to the index at which it is.
 *
 * @param count Amount of background items being shown.
 * @param index Index at which the background item whose unscaled Y offset will be scaled is.
 * @param height Height of the background item, as seen by the parent layout.
 * @param scale Amount by which the unscaled Y offset will be scaled.
 * @throws IllegalArgumentException If the [index] isn't that of a background item, as per
 *   [requireBackgroundItemIndex]'s documentation.
 * @see calculateUnscaledYOffsetForItem
 */
@Throws(IllegalArgumentException::class)
private fun calculateScaledYOffsetForBackgroundItem(
  count: Int,
  @BackgroundItemIndex index: Int,
  height: Int,
  @UnitFraction scale: Float = calculateScaleForBackgroundItem(count, index)
): Int {
  requireBackgroundItemIndex(index)
  val unscaledYOffset = calculateUnscaledYOffsetForItem(index, height)
  return (unscaledYOffset * scale).roundToInt()
}

/**
 * Calculates the unscaled offset in the Y axis of an item (either a foreground or a background
 * one).
 *
 * @param index Index at which the item to be placed is.
 * @param height Height of the item, as seen by the parent layout.
 * @see InitialBackgroundItemYOffsetFraction
 */
private fun calculateUnscaledYOffsetForItem(index: Int, height: Int): Int {
  return if (index == 0) 0 else (InitialBackgroundItemYOffsetFraction / index * height).roundToInt()
}

/**
 * Calculates the scale for a background item.
 *
 * @param count Amount of background items being shown.
 * @param index Index at which the background item to be scaled is.
 * @throws IllegalArgumentException If the [index] isn't that of a background item, as per
 *   [requireBackgroundItemIndex]'s documentation.
 */
@Throws(IllegalArgumentException::class)
@UnitFraction
private fun calculateScaleForBackgroundItem(count: Int, @BackgroundItemIndex index: Int): Float {
  val reversedIndex = calculateReversedIndexForBackgroundItem(count, index)
  return InitialBackgroundItemScale.pow(reversedIndex)
}

/**
 * Calculates the reversed index of a background item, as if the stacking order was the inverse of
 * what it is.
 *
 * In a [Stack] with 3 items, for example, one background item and two background ones, the item
 * that was added first (which would visually be the furthermost item) would normally be indexed as
 * 0; this method, on the other hand, returns 2 for it.
 *
 * @param count Amount of background items being shown.
 * @param index Index to be reversed, at which the background item is.
 * @throws IllegalArgumentException If the [index] isn't that of a background item, as per
 *   [requireBackgroundItemIndex]'s documentation.
 */
@Throws(IllegalArgumentException::class)
private fun calculateReversedIndexForBackgroundItem(
  count: Int,
  @BackgroundItemIndex index: Int
): Int {
  requireBackgroundItemIndex(index)
  return count - index
}

/**
 * Requires the given [index] to be that of a [Stack]'s background item, meaning that it should be
 * an [Int] within the `0..<`[MaxVisibleBackgroundItemCount] range. It starts at 0 because the item
 * that has been added lastly, the foreground one, is the one that is the most visible and has the
 * greatest index. That essentially means that each added item will be on top of the previously
 * added one and will have an increased index compared to the one preceding it.
 *
 * The furthermost background item, located at index `n -
 * `[FurthermostVisibleBackgroundItemIndexSubtrahend] within the entire item [List] (`n` being the
 * total amount of both foreground and background items), has an index of 0 among the items that are
 * actually visible (which does not always encompass every added one).
 *
 * @param index Index at which the background item is.
 * @throws IllegalArgumentException If the index isn't that of a background item (that is, isn't
 *   within the `0..<`[MaxVisibleBackgroundItemCount]` range).
 * @see Int.inc
 */
@Throws(IllegalArgumentException::class)
internal fun requireBackgroundItemIndex(@BackgroundItemIndex index: Int) {
  require(index >= 0) { "Index of a background item should be >= 0." }
  require(index < MaxVisibleBackgroundItemCount) {
    "Index of a background item cannot be greater than $MaxVisibleBackgroundItemCount."
  }
}

/**
 * Calculates a coordinate in the X axis for a background item to be horizontally centered within
 * its parent.
 *
 * @param parentWidth Width of the parent layout in which the child will be placed.
 * @param backgroundItemWidth Width of the child layout to be placed.
 */
private fun calculateCenteringXForBackgroundItem(parentWidth: Int, backgroundItemWidth: Int): Int {
  return (parentWidth - backgroundItemWidth) / 2
}

/** Preview of a [Stack]. */
@Composable
@MultiThemePreview
private fun StackPreview() {
  AutosTheme {
    Stack {
      item { StatusCard(rememberStatusCardState(Status.Failed)) { Text("Card #1") } }
      item { StatusCard(rememberStatusCardState(Status.Failed)) { Text("Card #2") } }
      item { StatusCard(rememberStatusCardState(Status.Succeeded)) { Text("Card #3") } }
    }
  }
}

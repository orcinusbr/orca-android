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

package com.jeanbarrossilva.orca.feature.onboarding.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.delay

/**
 * Duration in milliseconds of the delay with which the first [Char] of a [String] animated as
 * [State] will be appended to its value and on which the appendix of the following ones is based.
 *
 * @see animateStringAsState
 * @see State.value
 */
@Stable internal const val AnimatedAsStateStringMomentumInMilliseconds = 56L

/**
 * Animates the [targetValue] by appending each of its [Char]s to the resulting [State]'s value
 * sequentially.
 *
 * @param targetValue [String] to be animated.
 * @see State.value
 */
@Composable
internal fun animateStringAsState(targetValue: String): State<String> {
  var index = remember(targetValue) { 0 }
  var delay = remember(index) { AnimatedAsStateStringMomentumInMilliseconds }
  return remember { mutableStateOf("") }
    .apply {
      LaunchedEffect(index) {
        while (index <= targetValue.lastIndex) {
          value += targetValue[index++]
          delay(delay)
          delay =
            calculateNextCharDelayInMillisecondsForAnimatedAsStateString(
              index,
              targetValue.lastIndex
            )
        }
      }
    }
}

/**
 * Calculates the duration in milliseconds of the delay with which the next [Char] of a [String]
 * animated as [State] should be appended to its current value.
 *
 * @param nextIndex Index of the next [Char] to be appended to the value.
 * @param lastIndex Index of the last [Char] within the target [String].
 * @see animateStringAsState
 * @see State.value
 */
internal fun calculateNextCharDelayInMillisecondsForAnimatedAsStateString(
  nextIndex: Int,
  lastIndex: Int
): Long {
  return lerp(
    start = AnimatedAsStateStringMomentumInMilliseconds,
    stop = 0,
    fraction = nextIndex.toFloat() / lastIndex.toFloat()
  )
}

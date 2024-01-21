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

package com.jeanbarrossilva.orca.platform.animator.animation.animatable

import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.ui.test.junit4.createComposeRule
import assertk.assertThat
import assertk.assertions.isTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

internal class AnimatableTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun waitsForAnimation() {
    val animatable = Animatable()
    var hasAnimationFinished = false
    runTest(@OptIn(ExperimentalCoroutinesApi::class) UnconfinedTestDispatcher()) {
      launch {
        animatable.waitForAnimation()
        hasAnimationFinished = true
      }
      launch {
        composeRule.setContent { animatable.Animate(fadeIn(tween())) {} }
        composeRule.mainClock.advanceTimeBy(milliseconds = DefaultDurationMillis.toLong())
        assertThat(hasAnimationFinished).isTrue()
      }
    }
  }
}

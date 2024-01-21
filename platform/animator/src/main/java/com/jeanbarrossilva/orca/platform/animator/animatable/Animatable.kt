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

package com.jeanbarrossilva.orca.platform.animator.animatable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.jeanbarrossilva.orca.platform.animator.animatable.timing.Timing
import com.jeanbarrossilva.orca.platform.animator.animatable.timing.immediately
import kotlin.time.Duration
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Schedules the execution of the animation of a [Composable]. */
class Animatable internal constructor() {
  /**
   * [MutableStateFlow] to which [Boolean]s that indicate whether the content has been made visible
   * are emitted.
   */
  private val visibilityMutableFlow = MutableStateFlow(false)

  /**
   * [StateFlow] to which [Boolean]s that indicate whether the content has been made visible are
   * emitted.
   */
  internal val visibilityFlow = visibilityMutableFlow.asStateFlow()

  /** Whether the content is currently visible. */
  internal var isVisible
    get() = visibilityFlow.value
    private set(isVisible) {
      visibilityMutableFlow.value = isVisible
    }

  /**
   * Shows the [content] while animating it with the given [transition].
   *
   * @param transition [EnterTransition] to animate the [content]'s visibility change.
   * @param timing [Timing] that dictates when the animation will start running.
   * @param delay Amount of time to be waited for until the [content] is made visible.
   * @param content [Composable] to be displayed.
   */
  @Composable
  fun Animate(
    transition: EnterTransition = EnterTransition.None,
    timing: AnimatableScope.() -> Timing = AnimatableScope::immediately,
    delay: Duration = Duration.ZERO,
    content: @Composable AnimatableScope.() -> Unit
  ) {
    val animationActivenessFlow = remember { MutableStateFlow(false) }
    val scope = remember(animationActivenessFlow) { AnimatableScope(animationActivenessFlow) }

    LaunchedEffect(scope) {
      isVisible = false
      scope.timing().time()
      delay(delay)
      isVisible = true
    }

    AnimatedVisibility(visibilityFlow.collectAsState().value, enter = transition) {
      @OptIn(ExperimentalAnimationApi::class)
      LaunchedEffect(this.transition.isRunning) {
        animationActivenessFlow.value = this@AnimatedVisibility.transition.isRunning
      }

      scope.content()
    }
  }
}

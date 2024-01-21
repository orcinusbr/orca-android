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
import com.jeanbarrossilva.orca.platform.animator.animatable.timing.Timing
import com.jeanbarrossilva.orca.platform.animator.animatable.timing.immediately
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Schedules the execution of the animation of a [Composable]. */
class Animatable internal constructor() {
  /** [MutableStateFlow] to which the current stage of the animation is emitted. */
  private val animationMutableFlow = MutableStateFlow(Animation.Idle)

  /** Stage in which the animation currently is. */
  private var animation by animationMutableFlow

  /** [StateFlow] to which the current stage of the animation is emitted. */
  internal val animationFlow = animationMutableFlow.asStateFlow()

  /**
   * Shows the [content] while animating it with the given [transition].
   *
   * @param transition [EnterTransition] to animate the [content]'s visibility change.
   * @param timing [Timing] that dictates when the animation will start running.
   * @param content [Composable] to be displayed.
   */
  @Composable
  fun Animate(
    transition: EnterTransition = EnterTransition.None,
    timing: Timing = immediately(),
    content: @Composable () -> Unit
  ) {
    val isVisible = animationMutableFlow.collectAsState().value >= Animation.Ignited

    LaunchedEffect(transition, timing, content) {
      animation = Animation.Idle
      timing.time()
      animation = Animation.Finished
    }

    AnimatedVisibility(isVisible, enter = transition) {
      @OptIn(ExperimentalAnimationApi::class)
      LaunchedEffect(this.transition.isRunning) {
        if (this@AnimatedVisibility.transition.isRunning) {
          animation = Animation.Running
        }
      }

      content()
    }
  }
}

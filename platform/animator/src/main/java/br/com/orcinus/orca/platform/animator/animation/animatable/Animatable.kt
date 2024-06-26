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

package br.com.orcinus.orca.platform.animator.animation.animatable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import br.com.orcinus.orca.ext.coroutines.await
import br.com.orcinus.orca.ext.coroutines.getValue
import br.com.orcinus.orca.ext.coroutines.setValue
import br.com.orcinus.orca.platform.animator.animation.Animation
import br.com.orcinus.orca.platform.animator.animation.timing.Timing
import br.com.orcinus.orca.platform.animator.animation.timing.immediately
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter

/**
 * Schedules the execution of the animation of a [Composable].
 *
 * @param initialAnimation Stage that is considered to be the starting one of an animation.
 */
sealed class Animatable(initialAnimation: Animation) {
  /** [MutableStateFlow] to which the current stage of the animation is emitted. */
  internal val animationFlow = MutableStateFlow(initialAnimation)

  /** [Animatable] that animates the entrance of its content. */
  internal class Moving : Animatable(initialAnimation = Animation.Idle) {
    /** Stage in which the animation currently is. */
    private var animation by animationFlow

    @Composable
    override fun Animate(
      transition: EnterTransition,
      timing: Timing,
      content: @Composable () -> Unit
    ) {
      LaunchedEffect(transition, timing, content) {
        animation = Animation.Idle
        timing.time()
        animation = Animation.Ended
      }

      AnimatedVisibility(
        visible = animationFlow.collectAsState().value >= Animation.Prepared,
        enter = transition
      ) {
        @OptIn(ExperimentalAnimationApi::class)
        LaunchedEffect(this.transition.isRunning) {
          if (this@AnimatedVisibility.transition.isRunning) {
            animation = Animation.Ongoing
          }
        }

        content()
      }
    }
  }

  /** [Animatable] that displays its content instantly, ignoring any specified animation. */
  internal class Still : Animatable(initialAnimation = Animation.Ended) {
    @Composable
    override fun Animate(
      transition: EnterTransition,
      timing: Timing,
      content: @Composable () -> Unit
    ) {
      content()
    }
  }

  /**
   * Shows the [content] while animating it with the given [transition].
   *
   * @param transition [EnterTransition] to animate the [content]'s visibility change.
   * @param timing [Timing] that dictates when the animation will start running.
   * @param content [Composable] to be displayed.
   */
  @Composable
  abstract fun Animate(transition: EnterTransition, timing: Timing, content: @Composable () -> Unit)

  /**
   * Shows the [content] immediately without an [EnterTransition].
   *
   * @param content [Composable] to be displayed.
   */
  @Composable
  fun Animate(content: @Composable () -> Unit) {
    Animate(EnterTransition.None, content)
  }

  /**
   * Shows the [content] immediately while animating it with the given [transition].
   *
   * @param transition [EnterTransition] to animate the [content]'s visibility change.
   * @param content [Composable] to be displayed.
   */
  @Composable
  fun Animate(transition: EnterTransition, content: @Composable () -> Unit) {
    Animate(transition, immediately(), content)
  }

  /** Suspends until the animation finishes running. */
  internal suspend fun waitForAnimation() {
    animationFlow.filter { it == Animation.Ended }.await()
  }
}

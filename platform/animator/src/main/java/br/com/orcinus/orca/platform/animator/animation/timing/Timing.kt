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

package br.com.orcinus.orca.platform.animator.animation.timing

import androidx.annotation.CallSuper
import br.com.orcinus.orca.platform.animator.animation.animatable.Animatable
import kotlin.time.Duration
import kotlinx.coroutines.delay

/** Indicates when an animation should be run. */
sealed class Timing {
  /** [Duration] to be waited for before the animation runs. */
  internal abstract val delay: Duration

  /** Indicates that an animation should be run immediately. */
  internal data class Immediate(override val delay: Duration = Duration.ZERO) : Timing() {
    override fun plus(delay: Duration): Timing {
      return Immediate(delay)
    }
  }

  /**
   * Indicates that an animation should be run after the given [animatable] has finished animating.
   *
   * @param animatable [Animatable] whose animation has to finish for the one's to which this
   *   [Timing] refers to to start.
   */
  internal data class Sequential(
    private val animatable: Animatable,
    override val delay: Duration = Duration.ZERO
  ) : Timing() {
    override fun plus(delay: Duration): Timing {
      return Sequential(animatable, delay)
    }

    override suspend fun time() {
      animatable.waitForAnimation()
      super.time()
    }
  }

  /**
   * Indicates that the animation should only be run after the given amount of time.
   *
   * @param delay [Duration] to wait for before the animation runs.
   */
  abstract operator fun plus(delay: Duration): Timing

  /**
   * Suspends until the condition specified by this [Timing] for the animation to be run is
   * satisfied.
   */
  @CallSuper
  internal open suspend fun time() {
    delay(delay)
  }
}

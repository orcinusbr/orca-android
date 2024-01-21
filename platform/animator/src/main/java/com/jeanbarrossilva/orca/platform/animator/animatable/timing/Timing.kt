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

package com.jeanbarrossilva.orca.platform.animator.animatable.timing

import com.jeanbarrossilva.orca.ext.coroutines.await
import com.jeanbarrossilva.orca.platform.animator.animatable.Animatable
import com.jeanbarrossilva.orca.platform.animator.animatable.Animation
import kotlinx.coroutines.flow.filter

/** Indicates when an animation should be run. */
sealed class Timing {
  /** Indicates that an animation should be run immediately. */
  internal data object Immediate : Timing() {
    override suspend fun time() {}
  }

  /**
   * Indicates that an animation should be run after the given [animatable] has finished animating.
   *
   * @param animatable [Animatable] whose animation has to finish for the one's to which this
   *   [Timing] refers to to start.
   */
  internal data class Sequential(private val animatable: Animatable) : Timing() {
    override suspend fun time() {
      animatable.animationFlow.filter { it == Animation.Finished }.await()
    }
  }

  /**
   * Suspends until the condition specified by this [Timing] for the animation to be run is
   * satisfied.
   */
  internal abstract suspend fun time()
}

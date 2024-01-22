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

package com.jeanbarrossilva.orca.platform.animator.animation

import com.jeanbarrossilva.orca.platform.animator.animation.animatable.Animatable

/** Indicates whether animations should be run or not. */
enum class Motion {
  /** Denotes that animations should be enabled and run as expected. */
  Moving {
    override fun createAnimatable(): Animatable {
      return Animatable.Moving()
    }
  },

  /**
   * Denotes that all animations should be disabled and that the content should be displayed
   * instantly.
   */
  Still {
    override fun createAnimatable(): Animatable {
      return Animatable.Still()
    }
  };

  /** Creates an [Animatable]. */
  internal abstract fun createAnimatable(): Animatable
}

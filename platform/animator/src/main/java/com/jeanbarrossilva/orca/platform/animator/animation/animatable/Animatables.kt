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

import com.jeanbarrossilva.orca.platform.animator.animation.Motion

/**
 * Allows for the provision of [Animatable]s via destructuring.
 *
 * @param motion [Motion] by which all [Animatable]s will be created.
 */
@JvmInline
value class Animatables internal constructor(private val motion: Motion) {
  /** Creates a first [Animatable]. */
  operator fun component1(): Animatable {
    return motion.createAnimatable()
  }

  /** Creates a second [Animatable]. */
  operator fun component2(): Animatable {
    return motion.createAnimatable()
  }

  /** Creates a third [Animatable]. */
  operator fun component3(): Animatable {
    return motion.createAnimatable()
  }

  /** Creates a fourth [Animatable]. */
  operator fun component4(): Animatable {
    return motion.createAnimatable()
  }

  /** Creates a fifth [Animatable]. */
  operator fun component5(): Animatable {
    return motion.createAnimatable()
  }

  /** Creates a sixth [Animatable]. */
  operator fun component6(): Animatable {
    return motion.createAnimatable()
  }

  /** Creates a seventh [Animatable]. */
  operator fun component7(): Animatable {
    return motion.createAnimatable()
  }

  /** Creates an eighth [Animatable]. */
  operator fun component8(): Animatable {
    return motion.createAnimatable()
  }

  /** Creates a ninth [Animatable]. */
  operator fun component9(): Animatable {
    return motion.createAnimatable()
  }

  /** Creates a tenth [Animatable]. */
  operator fun component10(): Animatable {
    return motion.createAnimatable()
  }

  /** Creates an eleventh [Animatable]. */
  operator fun component11(): Animatable {
    return motion.createAnimatable()
  }

  /** Creates a twelfth [Animatable]. */
  operator fun component12(): Animatable {
    return motion.createAnimatable()
  }

  /** Creates a thirteenth [Animatable]. */
  operator fun component13(): Animatable {
    return motion.createAnimatable()
  }

  /** Creates a fourteenth [Animatable]. */
  operator fun component14(): Animatable {
    return motion.createAnimatable()
  }

  /** Creates a fifteenth [Animatable]. */
  operator fun component15(): Animatable {
    return motion.createAnimatable()
  }

  /** Creates a sixteenth [Animatable]. */
  operator fun component16(): Animatable {
    return motion.createAnimatable()
  }
}

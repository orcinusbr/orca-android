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

import com.jeanbarrossilva.orca.platform.animator.animatable.Animatable

/** [Timing] that indicates that an animation should be run immediately. */
fun immediately(): Timing {
  return Timing.Immediate()
}

/**
 * [Timing] that indicates that an animation should be run after the given [animatable] has finished
 * animating.
 *
 * @param animatable [Animatable] whose animation has to finish for the one to which this [Timing]
 *   refers to to start.
 */
fun after(animatable: Animatable): Timing {
  return Timing.Sequential(animatable)
}

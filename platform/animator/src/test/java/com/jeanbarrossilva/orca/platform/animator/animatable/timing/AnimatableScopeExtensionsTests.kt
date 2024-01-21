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

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.platform.animator.animatable.Animatable
import kotlin.test.Test

internal class AnimatableScopeExtensionsTests {
  @Test
  fun createsImmediateTiming() {
    assertThat(immediately()).isEqualTo(Timing.Immediate)
  }

  @Test
  fun createsSequentialTiming() {
    val animatable = Animatable()
    assertThat(after(animatable)).isEqualTo(Timing.Sequential(animatable))
  }
}

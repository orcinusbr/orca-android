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

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.platform.animator.animation.animatable.Animatable
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

internal class TimingExtensionsTests {
  @Test
  fun createsImmediateTiming() {
    assertThat(immediately()).isEqualTo(Timing.Immediate())
  }

  @Test
  fun createsDelayedTiming() {
    assertThat(after(2.seconds)).isEqualTo(Timing.Immediate(delay = 2.seconds))
  }

  @Test
  fun createsSequentialTiming() {
    val animatable = Animatable.Still()
    assertThat(after(animatable)).isEqualTo(Timing.Sequential(animatable))
  }
}

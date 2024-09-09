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

package br.com.orcinus.orca.app.activity.masking.scope

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isSameAs
import br.com.orcinus.orca.app.activity.masking.HardwareRoundedCorners
import br.com.orcinus.orca.app.activity.masking.Masker
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class MaskerScopeTests {
  @Test
  fun unsetMaskRadiiContainsEightNaNs() {
    assertThat(MaskerScope.UNSET_MASK_RADII)
      .containsExactly(
        Float.NaN,
        Float.NaN,
        Float.NaN,
        Float.NaN,
        Float.NaN,
        Float.NaN,
        Float.NaN,
        Float.NaN
      )
  }

  @Test
  fun runsBodyOnce() {
    var bodyRunCount = 0
    runMaskerTest { bodyRunCount++ }
    assertThat(bodyRunCount).isEqualTo(1)
  }

  @Test
  fun viewIsInitiallyUnmasked() {
    runMaskerTest { assertThat(maskRadii).isSameAs(MaskerScope.UNSET_MASK_RADII) }
  }

  @Test
  fun getsMaskRadii() {
    runMaskerTest {
      Masker.mask(HardwareRoundedCorners.Builder().bottomRight(4f).bottomLeft(2f).build(), view)
      assertThat(maskRadii)
        .containsExactly(
          0f,
          0f,
          0f,
          0f,
          4f,
          4f,
          2f,
          2f,
        )
    }
  }
}

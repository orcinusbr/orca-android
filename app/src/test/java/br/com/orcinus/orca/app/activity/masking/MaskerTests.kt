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

package br.com.orcinus.orca.app.activity.masking

import android.os.Build
import assertk.assertThat
import assertk.assertions.containsExactly
import br.com.orcinus.orca.app.activity.masking.scope.runMaskerTest
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
internal class MaskerTests {
  @Test
  fun masksWithRoundedCornersWhenTheyAreAvailable() {
    runMaskerTest {
      Masker.mask(HardwareRoundedCorners.Builder().bottomRight(4f).bottomLeft(2f).build(), view)
      assertThat(maskRadii).containsExactly(0f, 0f, 0f, 0f, 4f, 4f, 2f, 2f)
    }
  }

  @Config(sdk = [Build.VERSION_CODES.P, Build.VERSION_CODES.Q, Build.VERSION_CODES.R])
  @Test
  fun masksWithDefaultFormWhenRoundedCornersAreUnsupported() {
    runMaskerTest {
      Masker.mask(view)
      assertThat(maskRadii)
        .containsExactly(
          0f,
          0f,
          0f,
          0f,
          Masker.defaultForm.bottomEnd,
          Masker.defaultForm.bottomEnd,
          Masker.defaultForm.bottomStart,
          Masker.defaultForm.bottomStart
        )
    }
  }

  @Test
  fun masksWithDefaultFormWhenRoundedCornersAreUnavailable() {
    runMaskerTest {
      Masker.mask(HardwareRoundedCorners.Builder().build(), view)
      assertThat(maskRadii)
        .containsExactly(
          0f,
          0f,
          0f,
          0f,
          Masker.defaultForm.bottomEnd,
          Masker.defaultForm.bottomEnd,
          Masker.defaultForm.bottomStart,
          Masker.defaultForm.bottomStart
        )
    }
  }
}

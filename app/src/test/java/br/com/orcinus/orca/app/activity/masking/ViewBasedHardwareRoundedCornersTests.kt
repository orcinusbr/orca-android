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
import android.view.View
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import br.com.orcinus.orca.platform.testing.context
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
internal class ViewBasedHardwareRoundedCornersTests {
  @Config(sdk = [Build.VERSION_CODES.P, Build.VERSION_CODES.Q, Build.VERSION_CODES.R])
  @Test
  fun areNotAvailableWhenApiLevelIsLowerThanS() {
    val view = View(context)
    val roundedCorners = ViewBasedHardwareRoundedCorners(view)
    assertThat(roundedCorners).all {
      prop(ViewBasedHardwareRoundedCorners::bottomRight).isEqualTo(Float.NaN)
      prop(ViewBasedHardwareRoundedCorners::bottomLeft).isEqualTo(Float.NaN)
    }
  }

  @Test
  fun returnsNaNBottomRightRadiusWhenRoundedCornersAreNotAvailable() {
    val view = View(context)
    val roundedCorners = ViewBasedHardwareRoundedCorners(view)
    assertThat(roundedCorners.bottomRight()).isEqualTo(Float.NaN)
  }

  @Test
  fun returnsNaNBottomLeftRadiusWhenRoundedCornersAreNotAvailable() {
    val view = View(context)
    val roundedCorners = ViewBasedHardwareRoundedCorners(view)
    assertThat(roundedCorners.bottomLeft()).isEqualTo(Float.NaN)
  }
}

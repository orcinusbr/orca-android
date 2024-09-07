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

import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import assertk.assertions.prop
import kotlin.test.Test

internal class HardwareRoundedCornersBuilderTests {
  @Test
  fun buildsUnavailableHardwareRoundedCornersWhenAvailabilityIsUnspecified() {
    assertThat(HardwareRoundedCorners.Builder().build())
      .prop(HardwareRoundedCorners::areAvailable)
      .isFalse()
  }

  @Test
  fun buildsAvailableHardwareRoundedCorners() {
    assertThat(HardwareRoundedCorners.Builder().areAvailable(true).build())
      .prop(HardwareRoundedCorners::areAvailable)
      .isTrue()
  }

  @Test
  fun buildsHardwareRoundedCornersWithNaNBottomRightOneWhenUnspecified() {
    assertThat(HardwareRoundedCorners.Builder().build())
      .prop(HardwareRoundedCorners::bottomRight)
      .isEqualTo(Float.NaN)
  }

  @Test
  fun buildsHardwareRoundedCornersWithSpecifiedBottomRightOne() {
    assertThat(HardwareRoundedCorners.Builder().bottomRight(2f).build())
      .prop(HardwareRoundedCorners::bottomRight)
      .isEqualTo(2f)
  }

  @Test
  fun buildsHardwareRoundedCornersWithNaNBottomLeftOneWhenUnspecified() {
    assertThat(HardwareRoundedCorners.Builder().build())
      .prop(HardwareRoundedCorners::bottomLeft)
      .isEqualTo(Float.NaN)
  }

  @Test
  fun buildsHardwareRoundedCornersWithSpecifiedBottomLeftOne() {
    assertThat(HardwareRoundedCorners.Builder().bottomLeft(2f).build())
      .prop(HardwareRoundedCorners::bottomLeft)
      .isEqualTo(2f)
  }

  @Test
  fun builds() {
    assertThat(
        HardwareRoundedCorners.Builder().areAvailable(true).bottomRight(4f).bottomLeft(2f).build()
      )
      .all {
        prop(HardwareRoundedCorners::areAvailable).isTrue()
        prop(HardwareRoundedCorners::bottomRight).isEqualTo(4f)
        prop(HardwareRoundedCorners::bottomLeft).isEqualTo(2f)
      }
  }
}

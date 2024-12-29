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

package br.com.orcinus.orca.composite.timeline.search.field.interop

import android.view.Gravity
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class AlignmentsTests {
  private val size = IntSize(width = 2, height = 2)
  private val space = IntSize(width = 4, height = 4)

  @Test
  fun convertsTopStartIntoGravity() =
    assertThat(Alignment.TopStart)
      .transform("asGravity") { it.asGravity(size, space, LayoutDirection.Ltr) }
      .isEqualTo(Gravity.START or Gravity.TOP)

  @Test
  fun convertsCustomTopStartIntoGravity() =
    assertThat(Alignment { _, _, _ -> IntOffset.Zero })
      .transform("asGravity") { it.asGravity(size, space, LayoutDirection.Ltr) }
      .isEqualTo(Gravity.START or Gravity.TOP)

  @Test
  fun convertsTopCenterIntoGravity() =
    assertThat(Alignment.TopCenter)
      .transform("asGravity") { it.asGravity(size, space, LayoutDirection.Ltr) }
      .isEqualTo(Gravity.CENTER_HORIZONTAL or Gravity.TOP)

  @Test
  fun convertsCustomTopCenterIntoGravity() =
    assertThat(
        Alignment { size, space, _ -> IntOffset(x = space.width / 2 - size.width / 2, y = 0) }
      )
      .transform("asGravity") { it.asGravity(size, space, LayoutDirection.Ltr) }
      .isEqualTo(Gravity.CENTER_HORIZONTAL or Gravity.TOP)

  @Test
  fun convertsTopEndIntoGravity() =
    assertThat(Alignment.TopEnd)
      .transform("asGravity") { it.asGravity(size, space, LayoutDirection.Ltr) }
      .isEqualTo(Gravity.TOP or Gravity.END)

  @Test
  fun convertsCustomTopEndIntoGravity() =
    assertThat(Alignment { size, space, _ -> IntOffset(x = space.width - size.width, y = 0) })
      .transform("asGravity") { it.asGravity(size, space, LayoutDirection.Ltr) }
      .isEqualTo(Gravity.TOP or Gravity.END)

  @Test
  fun convertsCenterStartIntoGravity() =
    assertThat(Alignment.CenterStart)
      .transform("asGravity") { it.asGravity(size, space, LayoutDirection.Ltr) }
      .isEqualTo(Gravity.START or Gravity.CENTER_VERTICAL)

  @Test
  fun convertsCustomCenterStartIntoGravity() =
    assertThat(
        Alignment { size, space, _ -> IntOffset(x = 0, y = space.height / 2 - size.height / 2) }
      )
      .transform("asGravity") { it.asGravity(size, space, LayoutDirection.Ltr) }
      .isEqualTo(Gravity.START or Gravity.CENTER_VERTICAL)

  @Test
  fun convertsCenterIntoGravity() =
    assertThat(Alignment.Center)
      .transform("asGravity") { it.asGravity(size, space, LayoutDirection.Ltr) }
      .isEqualTo(Gravity.CENTER)

  @Test
  fun convertsCustomCenterIntoGravity() =
    assertThat(
        Alignment { size, space, _ ->
          IntOffset(x = space.width / 2 - size.width / 2, y = space.height / 2 - size.height / 2)
        }
      )
      .transform("asGravity") { it.asGravity(size, space, LayoutDirection.Ltr) }
      .isEqualTo(Gravity.CENTER)

  @Test
  fun convertsCenterEndIntoGravity() =
    assertThat(Alignment.CenterEnd)
      .transform("asGravity") { it.asGravity(size, space, LayoutDirection.Ltr) }
      .isEqualTo(Gravity.CENTER_VERTICAL or Gravity.END)

  @Test
  fun convertsCustomCenterEndIntoGravity() =
    assertThat(
        Alignment { size, space, _ ->
          IntOffset(x = space.width - size.width, y = space.height / 2 - size.height / 2)
        }
      )
      .transform("asGravity") { it.asGravity(size, space, LayoutDirection.Ltr) }
      .isEqualTo(Gravity.CENTER_VERTICAL or Gravity.END)

  @Test
  fun convertsBottomEndIntoGravity() =
    assertThat(Alignment.BottomEnd)
      .transform("asGravity") { it.asGravity(size, space, LayoutDirection.Ltr) }
      .isEqualTo(Gravity.END or Gravity.BOTTOM)

  @Test
  fun convertsCustomBottomEndIntoGravity() =
    assertThat(
        Alignment { size, space, _ ->
          IntOffset(x = space.width - size.width, y = space.height - size.height)
        }
      )
      .transform("asGravity") { it.asGravity(size, space, LayoutDirection.Ltr) }
      .isEqualTo(Gravity.END or Gravity.BOTTOM)

  @Test
  fun convertsBottomCenterIntoGravity() =
    assertThat(Alignment.BottomCenter)
      .transform("asGravity") { it.asGravity(size, space, LayoutDirection.Ltr) }
      .isEqualTo(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)

  @Test
  fun convertsCustomBottomCenterIntoGravity() =
    assertThat(
        Alignment { size, space, _ ->
          IntOffset(x = space.width / 2 - size.width / 2, y = space.height - size.height)
        }
      )
      .transform("asGravity") { it.asGravity(size, space, LayoutDirection.Ltr) }
      .isEqualTo(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)

  @Test
  fun convertsBottomStartIntoGravity() =
    assertThat(Alignment.BottomStart)
      .transform("asGravity") { it.asGravity(size, space, LayoutDirection.Ltr) }
      .isEqualTo(Gravity.START or Gravity.BOTTOM)

  @Test
  fun convertsCustomBottomStartIntoGravity() =
    assertThat(Alignment { size, space, _ -> IntOffset(x = 0, y = space.height - size.height) })
      .transform("asGravity") { it.asGravity(size, space, LayoutDirection.Ltr) }
      .isEqualTo(Gravity.START or Gravity.BOTTOM)
}

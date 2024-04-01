/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.composite.timeline.figure.gallery.disposition

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.ui.unit.dp
import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.autos.Spacings
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.disposition.withoutBottomEnd
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.disposition.withoutBottomStart
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.disposition.withoutTopEnd
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.disposition.withoutTopStart
import kotlin.test.Test

internal class CornerBasedShapeExtensionsTests {
  @Test
  fun zeroesTopStart() {
    val shape = RoundedCornerShape(Spacings.default.medium.dp)
    assertThat(shape.withoutTopStart)
      .isEqualTo(
        RoundedCornerShape(
          topStart = ZeroCornerSize,
          shape.topEnd,
          shape.bottomEnd,
          shape.bottomStart
        )
      )
  }

  @Test
  fun zeroesTopEnd() {
    val shape = RoundedCornerShape(Spacings.default.medium.dp)
    assertThat(shape.withoutTopEnd)
      .isEqualTo(
        RoundedCornerShape(
          shape.topStart,
          topEnd = ZeroCornerSize,
          shape.bottomEnd,
          shape.bottomStart
        )
      )
  }

  @Test
  fun zeroesBottomEnd() {
    val shape = RoundedCornerShape(Spacings.default.medium.dp)
    assertThat(shape.withoutBottomEnd)
      .isEqualTo(
        RoundedCornerShape(
          shape.topStart,
          shape.topEnd,
          bottomEnd = ZeroCornerSize,
          shape.bottomStart
        )
      )
  }

  @Test
  fun zeroesBottomStart() {
    val shape = RoundedCornerShape(Spacings.default.medium.dp)
    assertThat(shape.withoutBottomStart)
      .isEqualTo(
        RoundedCornerShape(
          shape.topStart,
          shape.topEnd,
          shape.bottomEnd,
          bottomStart = ZeroCornerSize
        )
      )
  }
}

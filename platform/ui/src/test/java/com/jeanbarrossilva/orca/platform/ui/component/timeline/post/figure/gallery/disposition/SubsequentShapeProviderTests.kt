/*
 * Copyright © 2023 Orca
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

package com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery.disposition

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

internal class SubsequentShapeProviderTests {
  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenAttachmentCountIsLessThanTwo() {
    Disposition.Grid.SubsequentShapeProvider.of(attachmentCount = 1, position = 2)
  }

  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenPositionIsLessThanTwo() {
    Disposition.Grid.SubsequentShapeProvider.of(attachmentCount = 2, position = 1)
  }

  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenPositionIsMoreThanThree() {
    Disposition.Grid.SubsequentShapeProvider.of(attachmentCount = 3, position = 4)
  }

  @Test
  fun getsTrailingOfTwo() {
    assertThat(Disposition.Grid.SubsequentShapeProvider.of(attachmentCount = 2, position = 2))
      .isEqualTo(Disposition.Grid.SubsequentShapeProvider.TRAILING_OF_TWO)
  }

  @Test
  fun getsSecondOfThree() {
    assertThat(Disposition.Grid.SubsequentShapeProvider.of(attachmentCount = 3, position = 2))
      .isEqualTo(Disposition.Grid.SubsequentShapeProvider.SECOND_OF_THREE)
  }

  @Test
  fun getsTrailingOfThree() {
    assertThat(Disposition.Grid.SubsequentShapeProvider.of(attachmentCount = 3, position = 3))
      .isEqualTo(Disposition.Grid.SubsequentShapeProvider.TRAILING_OF_THREE)
  }
}

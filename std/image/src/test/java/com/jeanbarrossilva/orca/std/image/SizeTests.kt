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

package com.jeanbarrossilva.orca.std.image

import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.prop
import kotlin.test.Test

internal class SizeTests {
  @Test
  fun createsSizeWithBothDimensionsBeingExplicit() {
    assertThat(ImageLoader.Size.explicit(width = 2, height = 4)).all {
      prop(ImageLoader.Size::width).isEqualTo(ImageLoader.Size.Dimension.Explicit(2))
      prop(ImageLoader.Size::height).isEqualTo(ImageLoader.Size.Dimension.Explicit(4))
    }
  }

  @Test
  fun createsSizeWithExplicitWidthAndAutomaticHeight() {
    assertThat(ImageLoader.Size.width(2)).all {
      prop(ImageLoader.Size::width).isEqualTo(ImageLoader.Size.Dimension.Explicit(2))
      prop(ImageLoader.Size::height).isInstanceOf<ImageLoader.Size.Dimension.Automatic>()
    }
  }

  @Test
  fun createsSizeWithExplicitHeightAndAutomaticWidth() {
    assertThat(ImageLoader.Size.height(4)).all {
      prop(ImageLoader.Size::width).isInstanceOf<ImageLoader.Size.Dimension.Automatic>()
      prop(ImageLoader.Size::height).isEqualTo(ImageLoader.Size.Dimension.Explicit(4))
    }
  }

  @Test
  fun widthIsFirstComponent() {
    assertThat(ImageLoader.Size.explicit(width = 2, height = 4).component1())
      .isEqualTo(ImageLoader.Size.Dimension.Explicit(2))
  }

  @Test
  fun heightIsSecondComponent() {
    assertThat(ImageLoader.Size.explicit(width = 2, height = 4).component2())
      .isEqualTo(ImageLoader.Size.Dimension.Explicit(4))
  }
}

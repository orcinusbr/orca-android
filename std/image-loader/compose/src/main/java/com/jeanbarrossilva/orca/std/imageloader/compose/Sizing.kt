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

package com.jeanbarrossilva.orca.std.imageloader.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Constraints
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * Defines how an [Image] should be sized.
 *
 * @see size
 */
enum class Sizing {
  /** Sizes the [Image] based on the dimensions of the [Composable]. */
  Constrained {
    override fun size(constraints: Constraints): ImageLoader.Size {
      return ImageLoader.Size.explicit(constraints.maxWidth, constraints.maxHeight)
    }
  },

  /**
   * Sizes the [Image] so that it fills the width of the [Composable] and automatically determines
   * its height.
   */
  Widened {
    override fun size(constraints: Constraints): ImageLoader.Size {
      return ImageLoader.Size.width(constraints.maxWidth)
    }
  },

  /**
   * Sizes the [Image] so that it fills the height of the [Composable] and automatically determines
   * its width.
   */
  Elongated {
    override fun size(constraints: Constraints): ImageLoader.Size {
      return ImageLoader.Size.height(constraints.maxHeight)
    }
  };

  /**
   * Gets a size that matches the way this [Sizing] sizes an [Image].
   *
   * @param constraints [Constraints] to which the [Composable] is delimited.
   */
  internal abstract fun size(constraints: Constraints): ImageLoader.Size
}

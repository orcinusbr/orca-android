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

package br.com.orcinus.orca.platform.testing.screen

import android.content.Context
import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.Objects

/**
 * Relevant information about the display of the device in which the testing is taking place.
 *
 * @param width [Dimension] for how wide the display is.
 * @param height [Dimension] for how tall the display is.
 */
class Screen private constructor(val width: Dimension, val height: Dimension) {
  override fun equals(other: Any?): Boolean {
    return other is Screen && width == other.width && height == other.height
  }

  override fun hashCode(): Int {
    return Objects.hash(width, height)
  }

  /**
   * Offers the value that represents the size of one of the axes of the [Screen] in multiple units.
   *
   * @param inPixels Value of this [Dimension] in pixels.
   * @param inDps Value of this [Dimension] in [Dp]s.
   */
  class Dimension private constructor(val inPixels: Int, val inDps: Dp) {
    override fun equals(other: Any?): Boolean {
      return other is Dimension && inPixels == other.inPixels && inDps == other.inDps
    }

    override fun hashCode(): Int {
      return Objects.hash(inPixels, inDps)
    }

    companion object {
      /**
       * Creates a width [Dimension].
       *
       * @param resources [Resources] from which the value in pixels and in [Dp]s will be obtained.
       */
      internal fun width(resources: Resources): Dimension {
        return Dimension(
          resources.displayMetrics.widthPixels,
          resources.configuration.screenWidthDp.dp
        )
      }

      /**
       * Creates a height [Dimension].
       *
       * @param resources [Resources] from which the value in pixels and in [Dp]s will be obtained.
       */
      internal fun height(resources: Resources): Dimension {
        return Dimension(
          resources.displayMetrics.heightPixels,
          resources.configuration.screenHeightDp.dp
        )
      }
    }
  }

  companion object {
    /**
     * Obtains test-tailored information about the display of the device.
     *
     * @param context [Context] whose [Resources] will provide the dimensions.
     */
    internal fun from(context: Context): Screen {
      return from(context.resources)
    }

    /**
     * Obtains test-tailored information about the display of the device.
     *
     * @param resources [Resources] by which the dimensions will be provided.
     */
    internal fun from(resources: Resources): Screen {
      val width = Dimension.width(resources)
      val height = Dimension.height(resources)
      return Screen(width, height)
    }
  }
}

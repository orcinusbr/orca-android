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

/** Provides the radii of a given region of the display (layout-direction-unwarily). */
interface HardwareRoundedCorners {
  /**
   * Entrypoint for constructing a set of rounded corners without explicitly subclassing
   * [HardwareRoundedCorners]. Particularly useful for tests, in which the instantiation process of
   * such a class should ideally be as concise as possible.
   */
  class Builder internal constructor() {
    /** Radius of the bottom right corner of the display. */
    private var bottomRight = Float.NaN

    /** Radius of the bottom left corner of the display. */
    private var bottomLeft = Float.NaN

    /** [HardwareRoundedCorners] instantiated by a [Builder]. */
    private inner class BuiltHardwareRoundedCorners : HardwareRoundedCorners {
      override fun bottomRight(): Float {
        return bottomRight
      }

      override fun bottomLeft(): Float {
        return bottomLeft
      }
    }

    /**
     * Defines the radius to be provided when that of the bottom right corner is obtained.
     *
     * @param bottomRight Bottom right corner radius to be set.
     */
    internal fun bottomRight(bottomRight: Float): Builder {
      this.bottomRight = bottomRight
      return this
    }

    /**
     * Defines the radius to be provided when that of the bottom left corner is obtained.
     *
     * @param bottomLeft Bottom left corner radius to be set.
     */
    internal fun bottomLeft(bottomLeft: Float): Builder {
      this.bottomLeft = bottomLeft
      return this
    }

    /** Builds an instance of [HardwareRoundedCorners] with the specified configuration. */
    internal fun build(): HardwareRoundedCorners {
      return BuiltHardwareRoundedCorners()
    }
  }

  /** Obtains the radius of the bottom right corner of the display. */
  fun bottomRight(): Float

  /** Obtains the radius of the bottom left corner of the display. */
  fun bottomLeft(): Float
}

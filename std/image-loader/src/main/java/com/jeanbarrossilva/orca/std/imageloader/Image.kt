/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.std.imageloader

import java.util.Objects

/**
 * An image.
 *
 * @param pixels [Pixel]s by which this [Image] is composed.
 */
@JvmInline
value class Image private constructor(val pixels: Array<Pixel>) {
  /** Amount of [Pixel]s in the x-axis. */
  val width
    get() = pixels.groupBy(Pixel::y)[0].orEmpty().size

  /** Amount of [Pixel]s in the y-axis. */
  val height
    get() = pixels.groupBy(Pixel::x)[0].orEmpty().size

  /**
   * Smallest addressable element in an [Image].
   *
   * @param x Location on the x-axis.
   * @param y Location on the y-axis.
   * @param color [Int] form of the color by which this [Pixel] is colored.
   */
  class Pixel internal constructor(val x: Int, val y: Int, val color: Int) {
    override fun equals(other: Any?): Boolean {
      return other is Pixel && x == other.x && y == other.y && color == other.color
    }

    override fun hashCode(): Int {
      return Objects.hash(x, y, color)
    }

    override fun toString(): String {
      return "Pixel(x=$x, y=$y, color=$color)"
    }
  }

  /**
   * Configures an [Image] to be built.
   *
   * @param width How wide the [Image] is.
   * @param height How tall the [Image] is.
   */
  class Builder internal constructor(private val width: Int, private val height: Int) {
    /** Final size of the [Image] to be built, calculated from the given [width] and [height]. */
    private val size = width * height

    /** [Pixel]s by which the [Image] will be composed. */
    private val pixels = arrayOfNulls<Pixel>(size)

    /** Index into which the next [Pixel] to be added will be put. */
    private var nextPixelIndex = 0

    /**
     * Adds a [Pixel] to the next remaining space in the x-axis, jumping to the next column if the
     * row has been filled.
     *
     * @param color [Int] form of the color by which the [Pixel] will be colored.
     * @throws IllegalArgumentException If the [Image] hasn't been entirely filled or the amount of
     *   [Pixel]s is greater than the [size].
     */
    fun pixel(color: Int) {
      val lastPixel = pixels.getOrNull(nextPixelIndex.dec())
      val x = lastPixel?.x?.inc()?.mirror(0, width.dec()) ?: 0
      val y = if (lastPixel != null && x == 0) lastPixel.y.inc() else lastPixel?.y ?: 0
      require(y < height) { "Coordinate out of $width x $height bounds: ($x, $y)." }
      pixels[nextPixelIndex++] = Pixel(x, y, color)
    }

    /** Builds the [Image] with the provided configurations. */
    internal fun build(): Image {
      ensurePixelSufficiency()
      nextPixelIndex = 0
      @Suppress("UNCHECKED_CAST") return Image(pixels as Array<Pixel>)
    }

    /**
     * Ensures that the added amount of [Pixel]s is sufficient for the [size] of the [Image] to be
     * built.
     *
     * @throws IllegalArgumentException If the [Image] hasn't been entirely filled.
     */
    private fun ensurePixelSufficiency() {
      require(nextPixelIndex == pixels.size) {
        throw IllegalArgumentException("Insufficient amount of pixels for $width x $height.")
      }
    }
  }
}

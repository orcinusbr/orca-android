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

package com.jeanbarrossilva.orca.std.imageloader

import java.util.Objects

/** [ImageLoader] with a generic source. */
typealias SomeImageLoader = ImageLoader<*>

/**
 * Loads an [Image] through [load].
 *
 * @param T Source from which the [Image] will be obtained.
 */
interface ImageLoader<T : Any> {
  /** Source from which the [Image] will be obtained. */
  val source: T

  /**
   * Provides an [ImageLoader] through [provide].
   *
   * @param T Source from which the [Image] will be obtained.
   */
  fun interface Provider<T : Any> {
    /**
     * Provides an [ImageLoader].
     *
     * @param source Source from which the [Image] will be obtained.
     */
    fun provide(source: T): ImageLoader<T>

    companion object
  }

  /**
   * Dimensions according to which an [Image] should be sized.
   *
   * @param width [Dimension] for how wide the [Image] is.
   * @param height [Dimension] for tall the [Image] is.
   */
  class Size
  @Throws(IllegalArgumentException::class)
  private constructor(val width: Dimension, val height: Dimension) {
    /**
     * Denotes whether a dimension is explicitly specified or should be calculated automatically.
     *
     * @see Automatic
     * @see Explicit
     */
    sealed interface Dimension {
      /**
       * Denotes that the value should be calculated based either on an explicitly defined one or
       * the constraints to which the [Image] to be sized is delimited.
       */
      data object Automatic : Dimension

      /**
       * Denotes that the value is the given one.
       *
       * @param value Value of this [Dimension] in pixels.
       */
      @JvmInline value class Explicit(val value: Int) : Dimension
    }

    init {
      require(width is Dimension.Explicit || height is Dimension.Explicit) {
        "Cannot size an image without at least one explicit dimension."
      }
    }

    override fun equals(other: Any?): Boolean {
      return other is Size && width == other.width && height == other.height
    }

    override fun hashCode(): Int {
      return Objects.hash(width, height)
    }

    override fun toString(): String {
      return "Size(width=$width, height=$height)"
    }

    operator fun component1(): Dimension {
      return width
    }

    operator fun component2(): Dimension {
      return height
    }

    companion object {
      /**
       * Creates a [Size] with both dimensions, [width] and [height], being explicit.
       *
       * @param width How wide the [Image] is.
       * @param height How tall the [Image] is.
       * @see Dimension.Explicit
       */
      fun explicit(width: Int, height: Int): Size {
        val wd = Dimension.Explicit(width)
        val hd = Dimension.Explicit(height)
        return Size(wd, hd)
      }

      /**
       * Creates a [Size] with an explicit [width] and an automatic [height].
       *
       * @param width How wide the [Image] is.
       */
      fun width(width: Int): Size {
        val wd = Dimension.Explicit(width)
        return Size(wd, Dimension.Automatic)
      }

      /**
       * Creates a [Size] with an explicit [height] and an automatic [height].
       *
       * @param height How tall the [Image] is.
       */
      fun height(height: Int): Size {
        val hd = Dimension.Explicit(height)
        return Size(Dimension.Automatic, hd)
      }
    }
  }

  /**
   * Loads an [Image].
   *
   * @param size [Size] in which the [Image] will be loaded.
   */
  suspend fun load(size: Size): Image?

  companion object
}

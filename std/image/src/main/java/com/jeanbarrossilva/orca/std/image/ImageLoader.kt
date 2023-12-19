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

/** [ImageLoader] with generic source and image. */
typealias SomeImageLoader = ImageLoader<*, *>

/**
 * [ImageLoader.Provider] with a generic image.
 *
 * @param T Source from which the image will be loaded.
 */
typealias SomeImageLoaderProvider<T> = ImageLoader.Provider<T, *>

/**
 * Loads an image through [load].
 *
 * @param I Source from which the image will be loaded.
 * @param O Image to be loaded.
 */
interface ImageLoader<I : Any, O : Any> {
  /** Source from which the image will be loaded. */
  val source: I

  /**
   * Provides an [ImageLoader] through [provide].
   *
   * @param PI Source from which the image will be loaded.
   * @param PO Image to be loaded.
   */
  fun interface Provider<PI : Any, PO : Any> {
    /**
     * Provides an [ImageLoader].
     *
     * @param source Source from which the image will be loaded.
     */
    fun provide(source: PI): ImageLoader<PI, PO>

    companion object
  }

  /** Loads the image. */
  fun load(): O

  companion object
}

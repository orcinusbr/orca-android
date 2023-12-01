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

/** [ImageLoader] with a generic source. */
typealias SomeImageLoader = ImageLoader<*>

/** [ImageLoader.Provider] with a generic source. */
typealias SomeImageLoaderProvider = ImageLoader.Provider<*>

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
   * Loads an [Image].
   *
   * @param width How wide the [Image] is.
   * @param height How tall the [Image] is.
   */
  suspend fun load(width: Int, height: Int): Image?

  companion object
}

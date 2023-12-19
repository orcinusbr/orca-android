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

package com.jeanbarrossilva.orca.std.image.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import com.jeanbarrossilva.orca.std.image.ImageLoader

/** [ComposableImageLoader] with a generic source. */
typealias SomeComposableImageLoader = ComposableImageLoader<*>

/** Image provided by a [ComposableImageLoader]. */
typealias ComposableImage =
  @Composable (contentDescription: String, Shape, ContentScale, Modifier) -> Unit

/**
 * [ImageLoader] that loads a [Composable] image.
 *
 * @param T Source from which the image will be loaded.
 */
abstract class ComposableImageLoader<T : Any> : ImageLoader<T, ComposableImage> {
  /**
   * [ImageLoader.Provider] that provides a [ComposableImageLoader].
   *
   * @param T Source from which the image will be loaded.
   */
  interface Provider<T : Any> : ImageLoader.Provider<T, ComposableImage> {
    override fun provide(source: T): ComposableImageLoader<T>

    companion object
  }

  companion object {
    /** [ContentScale] to be applied to an image by default. */
    val DefaultContentScale = ContentScale.Crop
  }
}

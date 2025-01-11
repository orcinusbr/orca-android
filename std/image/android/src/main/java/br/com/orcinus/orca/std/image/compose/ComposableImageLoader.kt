/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.std.image.compose

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.android.AndroidImage
import br.com.orcinus.orca.std.image.android.AndroidImageLoader
import java.net.URI

/** [AndroidImageLoader] with a generic source. */
@Deprecated(
  "As of Orca 0.5.0, the Android-specific image loader encompasses both UI frameworks (the View " +
    "system and Jetpack Compose), with an image being a drawable and a composable.",
  ReplaceWith(
    "AndroidImageLoader",
    imports = ["br.com.orcinus.orca.std.image.android.AndroidImageLoader"]
  )
)
typealias SomeComposableImageLoader = AndroidImageLoader<*>

/**
 * [ImageLoader] that loads a [Composable] image.
 *
 * @param T Source from which the image will be loaded.
 */
@Deprecated(
  "As of Orca 0.5.0, the Android-specific image loader encompasses both UI frameworks (the View " +
    "system and Jetpack Compose), with an image being a drawable or a composable.",
  ReplaceWith(
    "AndroidImageLoader",
    imports = ["br.com.orcinus.orca.std.image.android.AndroidImageLoader"]
  )
)
abstract class ComposableImageLoader<T : Any> : AndroidImageLoader<T>() {
  /**
   * [ImageLoader.Provider] that provides a [ComposableImageLoader].
   *
   * @param T Source from which the image will be loaded.
   */
  sealed interface Provider<T : Any> : AndroidImageLoader.Provider<T> {
    companion object : AndroidImageLoader.Provider.CompanionCompat()
  }

  companion object : CompanionCompat() {
    /**
     * [ContentScale] to be applied to an image's composable by default.
     *
     * @see AndroidImage.asComposable
     */
    @Deprecated(
      "As of Orca 0.5.0, the Android-specific image loader encompasses both UI frameworks (the " +
        "View system and Jetpack Compose), with an image being a drawable or a composable. Thus, " +
        "this property's name is ambiguous, since it does not make explicit that such content " +
        "scale is specific to the latter.",
      ReplaceWith(
        "AndroidImage.DefaultComposableContentScale",
        imports = ["br.com.orcinus.orca.std.image.android.AndroidImage"]
      )
    )
    val DefaultContentScale = AndroidImage.DefaultComposableContentScale
  }
}

/**
 * Remembers an [AndroidImageLoader].
 *
 * @param source Resource ID from which the image will be obtained.
 */
@Composable
fun rememberImageLoader(@DrawableRes source: Int) =
  br.com.orcinus.orca.std.image.android.rememberImageLoader(source)

/**
 * Remembers an [AndroidImageLoader].
 *
 * @param source [URI] from which the image will be obtained.
 */
@Composable
fun rememberImageLoader(source: URI) =
  br.com.orcinus.orca.std.image.android.rememberImageLoader(source)

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

package br.com.orcinus.orca.std.image.android

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.android.async.AsyncImageLoader
import br.com.orcinus.orca.std.image.android.local.LocalImageLoader
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import java.lang.ref.WeakReference
import java.net.URI

/**
 * [ImageLoader] that loads an Android-specific image.
 *
 * @param T Source from which the image will be loaded.
 */
abstract class AndroidImageLoader<T : Any> : ImageLoader<T, AndroidImage> {
  /**
   * [ImageLoader.Provider] that provides an [AndroidImageLoader].
   *
   * @param T Source from which the image will be loaded.
   */
  interface Provider<T : Any> : ImageLoader.Provider<T, AndroidImage> {
    /**
     * Class extended by the companion objects of [AndroidImageLoader] and [ComposableImageLoader]
     * whose only purpose is to allow for older references to the latter to remain existent,
     * preventing them from _having_ to be updated (although adding them from now on is
     * discouraged).
     */
    abstract class CompanionCompat internal constructor()

    override fun provide(source: T): AndroidImageLoader<T>

    companion object : CompanionCompat()
  }

  /**
   * Class extended by [AndroidImageLoader] and [ComposableImageLoader] whose only purpose is to
   * allow for older references to the latter to remain existent, preventing them from _having_ to
   * be updated (although adding them from now on is discouraged).
   */
  abstract class CompanionCompat internal constructor()

  /** Creates an empty [AndroidImage]. */
  fun createImage() = AndroidImage.Builder()

  companion object : CompanionCompat()
}

/**
 * Image loaded by an [AndroidImageLoader], which aims to be providable to either frameworks with
 * which UI can be constructed on Android: the [View] system, or Jetpack Compose. Whether this has
 * been (or will be) obtained locally or remotely will depend on the source.
 *
 * @see AndroidImageLoader.source
 */
abstract class AndroidImage private constructor() {
  /**
   * Creation entrypoint of an [AndroidImage] through which its [Drawable] and composable versions
   * can be defined (either can be left unset and, by default, are respectively `null` and nothing).
   * Instantiable from an [AndroidImageLoader].
   */
  class Builder internal constructor() : AndroidImage() {
    /** [AndroidImage.asDrawable] lambda. */
    private var asDrawable: suspend () -> Drawable? = { null }

    /** [AndroidImage.asComposable] lambda. */
    private var asComposable:
      @Composable
      (Modifier, contentDescription: String, Shape, ContentScale) -> Unit =
      { _, _, _, _ ->
      }

    override suspend fun asDrawable() = asDrawable.invoke()

    /**
     * Defines the [Drawable] version of the image to be obtained.
     *
     * @param asDrawable Provider to be set.
     */
    fun asDrawable(asDrawable: suspend () -> Drawable?) = apply { this.asDrawable = asDrawable }

    @Composable
    override fun asComposable(
      modifier: Modifier,
      contentDescription: String,
      shape: Shape,
      contentScale: ContentScale
    ) = asComposable.invoke(modifier, contentDescription, shape, contentScale)

    /**
     * Defines the composable version of the image.
     *
     * @param asComposable Composable to be displayed.
     */
    fun asComposable(
      asComposable: @Composable (Modifier, contentDescription: String, Shape, ContentScale) -> Unit
    ) = apply { this.asComposable = asComposable }
  }

  /** Obtains the [Drawable] version of this image. */
  abstract suspend fun asDrawable(): Drawable?

  /**
   * Alias for `asComposable(modifier, contentDescription, shape, contentScale)`.
   *
   * @param contentDescription Describes the contents of this image.
   * @param shape [Shape] by which this image is clipped.
   * @param modifier [Modifier] to be applied to this image.
   * @param contentScale Defines how this image is scaled.
   */
  @Composable
  @Deprecated(
    "As of Orca 0.5.0, the Android-specific image encompasses both UI frameworks (the View " +
      "system and Jetpack Compose) and is a drawable and a composable. This method exists solely " +
      "for backwards compatibility and, for clarity, calls to it should be replaced by ones to " +
      "either overloads of asComposable.",
    ReplaceWith("asComposable(modifier, contentDescription, shape, contentScale)")
  )
  operator fun invoke(
    contentDescription: String,
    shape: Shape,
    contentScale: ContentScale,
    modifier: Modifier
  ) = asComposable(modifier, contentDescription, shape, contentScale)

  /**
   * Composable version of this image.
   *
   * @param contentDescription Describes the contents of this image.
   * @param shape [Shape] by which this image is clipped.
   * @param modifier [Modifier] to be applied to this image.
   * @param contentScale Defines how this image is scaled.
   */
  @Composable
  @Suppress("ComposableNaming")
  abstract fun asComposable(
    modifier: Modifier,
    contentDescription: String,
    shape: Shape,
    contentScale: ContentScale
  )

  companion object {
    /**
     * [ContentScale] to be applied to the composable version of the image by default.
     *
     * @see asComposable
     */
    @JvmField val DefaultComposableContentScale = ContentScale.Crop
  }
}

/**
 * Composable version of this image.
 *
 * @param contentDescription Describes the contents of this image.
 * @param shape [Shape] by which this image is clipped.
 * @param modifier [Modifier] to be applied to this image.
 * @param contentScale Defines how this image is scaled.
 */
@Composable
@Suppress("ComposableNaming")
fun AndroidImage.asComposable(
  contentDescription: String,
  shape: Shape,
  modifier: Modifier = Modifier,
  contentScale: ContentScale = AndroidImage.DefaultComposableContentScale
) = asComposable(modifier, contentDescription, shape, contentScale)

/**
 * Remembers an [AndroidImageLoader].
 *
 * @param source Resource ID from which the image will be obtained.
 */
@Composable
fun rememberImageLoader(@DrawableRes source: Int): AndroidImageLoader<*> {
  val context = LocalContext.current
  return remember(context, source) { LocalImageLoader(WeakReference(context), source) }
}

/**
 * Remembers an [AndroidImageLoader].
 *
 * @param source [URI] from which the image will be obtained.
 */
@Composable
fun rememberImageLoader(source: URI): AndroidImageLoader<*> {
  val context = LocalContext.current
  return remember(source) { AsyncImageLoader.Provider(WeakReference(context)).provide(source) }
}

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

package br.com.orcinus.orca.platform.core.image

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.Discouraged
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.std.image.android.AndroidImageLoader
import br.com.orcinus.orca.std.image.compose.async.AsyncImageLoader
import java.lang.ref.WeakReference
import java.util.Objects

/** Sample [AndroidImageLoader.Provider]. */
@Deprecated(
  "Obtaining a sample image loader implicitly involves instantiating one; thus, calling this " +
    "property's getter multiple times creates multiple sample image loaders. Prefer calling the " +
    "factory method for clarity.",
  ReplaceWith("createSample()", imports = ["br.com.orcinus.orca.platform.core.image.createSample"])
)
val AndroidImageLoader.Provider.CompanionCompat.sample:
  AndroidImageLoader.Provider<SampleImageSource>
  @Suppress("DiscouragedApi") get() = createSample()

/**
 * [AndroidImageLoader] that loads images from a [SampleImageSource].
 *
 * @property contextRef [WeakReference] to the [Context] from which the [Drawable] version of the
 *   image is obtained.
 */
internal class SampleAndroidImageLoader
private constructor(
  private val contextRef: WeakReference<Context>,
  override val source: SampleImageSource
) : AndroidImageLoader<SampleImageSource>() {
  /**
   * [AndroidImageLoader.Provider] that provides a [SampleAndroidImageLoader].
   *
   * @property contextRef [WeakReference] to the [Context] from which the [Drawable] version of the
   *   image is obtained.
   */
  class Provider(private val contextRef: WeakReference<Context>) :
    AndroidImageLoader.Provider<SampleImageSource> {
    override fun provide(source: SampleImageSource): SampleAndroidImageLoader {
      return SampleAndroidImageLoader(contextRef, source)
    }
  }

  override fun equals(other: Any?) =
    other is SampleAndroidImageLoader && contextRef == other.contextRef && source == other.source

  override fun hashCode() = Objects.hash(contextRef, source)

  override fun load() =
    createImage()
      .asDrawable { contextRef.get()?.let { ContextCompat.getDrawable(it, source.resourceID) } }
      .asComposable { modifier, contentDescription, shape, contentScale ->
        Image(
          painterResource(source.resourceID),
          contentDescription,
          modifier.clip(shape),
          contentScale = contentScale
        )
      }
}

/**
 * Creates a sample [AndroidImageLoader].
 *
 * @param source [SampleImageSource] from which the image will be loaded.
 */
@Composable
@Suppress("UnusedReceiverParameter")
fun AndroidImageLoader.CompanionCompat.createSample(
  source: SampleImageSource
): AndroidImageLoader<SampleImageSource> {
  return AndroidImageLoader.Provider.createSample(LocalContext.current).provide(source)
}

/**
 * Creates a sample [AndroidImageLoader.Provider].
 *
 * Alias for `createSample(WeakReference(null))`.
 *
 * @property context [Context] from which the [Drawable] version of the image is obtained.
 */
@Discouraged(
  "Drawables of images are unobtainable when they are loaded by loaders provided by context-less " +
    "providers. Prefer calling the overload of this factory function which receives a context " +
    "instead."
)
fun AndroidImageLoader.Provider.CompanionCompat.createSample() =
  createSample(AsyncImageLoader.clearedContextRef)

/**
 * Creates a sample [AndroidImageLoader.Provider].
 *
 * Alias for `createSample(WeakReference(context))`.
 *
 * @property context [Context] from which the [Drawable] version of the image is obtained.
 */
fun AndroidImageLoader.Provider.CompanionCompat.createSample(context: Context) =
  createSample(WeakReference(context))

/**
 * Creates a sample [AndroidImageLoader.Provider].
 *
 * @property contextRef [WeakReference] to the [Context] from which the [Drawable] version of the
 *   image is obtained.
 */
@Suppress("UnusedReceiverParameter")
fun AndroidImageLoader.Provider.CompanionCompat.createSample(
  contextRef: WeakReference<Context>
): AndroidImageLoader.Provider<SampleImageSource> = SampleAndroidImageLoader.Provider(contextRef)

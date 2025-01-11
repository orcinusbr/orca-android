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

package br.com.orcinus.orca.std.image.compose.async

import android.content.Context
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.android.AndroidImage
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import java.lang.ref.WeakReference
import java.net.URI

/** [ComposableImageLoader] that loads an image asynchronously. */
@Deprecated(
  "Drawables of images loaded by this async loader are unobtainable because it is context-less.",
  ReplaceWith(
    "AsyncImageLoader",
    imports = ["br.com.orcinus.orca.std.image.android.async.AsyncImageLoader"]
  )
)
class AsyncImageLoader private constructor(override val source: URI) :
  br.com.orcinus.orca.std.image.android.AsyncImageLoader() {
  override val contextRef = clearedContextRef

  /** [ImageLoader.Provider] that provides an [AsyncImageLoader]. */
  @Deprecated(
    "Drawables of images loaded by async loaders provided by this provider are unobtainable " +
      "because they are context-less.",
    ReplaceWith(
      "AsyncImageLoader.Provider",
      imports = ["br.com.orcinus.orca.std.image.android.async.AsyncImageLoader"]
    )
  )
  object Provider : ImageLoader.Provider<URI, AndroidImage> {
    override fun provide(source: URI): AsyncImageLoader {
      return AsyncImageLoader(source)
    }
  }

  companion object {
    /** [WeakReference] to a cleared [Context]. */
    @JvmField val clearedContextRef = WeakReference<Context>(null)
  }
}

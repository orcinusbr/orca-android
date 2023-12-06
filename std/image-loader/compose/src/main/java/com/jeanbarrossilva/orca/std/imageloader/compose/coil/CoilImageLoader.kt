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

package com.jeanbarrossilva.orca.std.imageloader.compose.coil

import android.content.Context
import coil.imageLoader
import coil.request.ImageRequest
import com.jeanbarrossilva.orca.std.imageloader.AsyncImageLoader
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.local.drawable.toImage
import java.net.URL

/**
 * [ImageLoader] powered by the Coil library.
 *
 * @param context [Context] through which [ImageRequest]s will be performed.
 */
class CoilImageLoader private constructor(private val context: Context, override val source: URL) :
  AsyncImageLoader() {
  /**
   * [ImageLoader.Provider] that provides a [CoilImageLoader].
   *
   * @param context [Context] through which the provided [CoilImageLoader]'s [ImageRequest]s will be
   *   performed.
   */
  class Provider(private val context: Context) : ImageLoader.Provider<URL> {
    override fun provide(source: URL): ImageLoader<URL> {
      return CoilImageLoader(context, source)
    }
  }

  override suspend fun load(size: ImageLoader.Size): Image? {
    val request = ImageRequest.Builder(context).data("$source").size(size.coil).build()
    return context.imageLoader.execute(request).drawable?.toImage(size)
  }
}

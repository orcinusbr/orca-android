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

package com.jeanbarrossilva.orca.std.imageloader.compose

import android.content.Context
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import com.jeanbarrossilva.orca.std.imageloader.AsyncImageLoader
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.local.toImage
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

  override suspend fun load(width: Int, height: Int): Image? {
    val request = ImageRequest.Builder(context).data("$source").size(width, height).build()
    return context.imageLoader.execute(request).drawable?.toBitmap()?.toImage()
  }
}

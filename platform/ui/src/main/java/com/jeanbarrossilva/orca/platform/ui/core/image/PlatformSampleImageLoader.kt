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

package com.jeanbarrossilva.orca.platform.ui.core.image

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.local.toImage

internal class PlatformSampleImageLoader
private constructor(private val context: Context, override val source: SampleImageSource) :
  ImageLoader<SampleImageSource> {
  class Provider(private val context: Context) : ImageLoader.Provider<SampleImageSource> {
    override fun provide(source: SampleImageSource): PlatformSampleImageLoader {
      return PlatformSampleImageLoader(context, source)
    }
  }

  override suspend fun load(width: Int, height: Int): Image? {
    return ContextCompat.getDrawable(context, source.resourceID)?.toBitmap(width, height)?.toImage()
  }
}

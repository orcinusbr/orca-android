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

package com.jeanbarrossilva.orca.std.imageloader.local

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * [ImageLoader] that loads an [Image] locally through its resource ID.
 *
 * @param context [Context] through which the underlying [Drawable] will be obtained.
 */
abstract class LocalImageLoader : ImageLoader<Int> {
  protected abstract val context: Context
  @get:DrawableRes abstract override val source: Int

  override suspend fun load(width: Int, height: Int): Image? {
    return context.getDrawable(source)?.toBitmap(width, height)?.toImage()
  }
}

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

import android.graphics.Bitmap
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.buildImage

/** Converts this [Bitmap] into an [Image]. */
suspend fun Bitmap.toImage(): Image {
  return buildImage(width, height) {
    val nonHardwareBitmap =
      if (config == Bitmap.Config.HARDWARE) {
        copy(Bitmap.Config.ARGB_8888, false)
      } else {
        this@toImage
      }
    IntArray(size = width * height)
      .also { nonHardwareBitmap.getPixels(it, 0, width, 0, 0, width, height) }
      .forEach(::pixel)
  }
}

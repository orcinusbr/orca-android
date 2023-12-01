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

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import com.jeanbarrossilva.orca.std.imageloader.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/** Converts this [Image] into a [Bitmap]. */
internal suspend fun Image.toBitmap(): Bitmap {
  return createBitmap(width, height).apply {
    withContext(Dispatchers.IO) {
      coroutineScope { pixels.forEach { setPixel(it.x, it.y, it.color) } }
    }
  }
}

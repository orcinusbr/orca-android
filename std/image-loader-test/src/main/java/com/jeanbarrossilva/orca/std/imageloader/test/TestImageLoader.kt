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

package com.jeanbarrossilva.orca.std.imageloader.test

import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.buildImage

/** [ImageLoader] that loads an empty [Image]. */
object TestImageLoader : ImageLoader<Unit> {
  override val source = Unit

  /** [ImageLoader.Provider] that provides the [TestImageLoader]. */
  object Provider : ImageLoader.Provider<Unit> {
    override fun provide(source: Unit): TestImageLoader {
      return TestImageLoader
    }
  }

  override suspend fun load(width: Int, height: Int): Image {
    return buildImage(width = 1, height = 1) { pixel(0) }
  }
}

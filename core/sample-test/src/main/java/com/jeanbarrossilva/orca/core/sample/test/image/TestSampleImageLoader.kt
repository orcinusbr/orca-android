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

package com.jeanbarrossilva.orca.core.sample.test.image

import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.buildImage

/** [ImageLoader] that loads an empty [Image] from a [SampleImageSource]. */
class TestSampleImageLoader private constructor() : ImageLoader<SampleImageSource> {
  override val source = SampleImageSource.None

  /** [ImageLoader.Provider] that provides a [TestSampleImageLoader]. */
  object Provider : ImageLoader.Provider<SampleImageSource> {
    override fun provide(source: SampleImageSource): TestSampleImageLoader {
      return instance
    }
  }

  override suspend fun load(width: Int, height: Int): Image {
    return buildImage(width = 1, height = 1) { pixel(0) }
  }

  companion object {
    /** Single instance of a [TestSampleImageLoader]. */
    private val instance = TestSampleImageLoader()
  }
}

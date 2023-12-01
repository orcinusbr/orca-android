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

package com.jeanbarrossilva.orca.std.imageloader

/** [ImageLoader] with a generic source. */
typealias SomeImageLoader = ImageLoader<*>

/** [ImageLoader.Provider] with a generic source. */
typealias SomeImageLoaderProvider = ImageLoader.Provider<*>

/**
 * Loads an [Image] through [load].
 *
 * @param T Source from which the [Image] will be obtained.
 */
interface ImageLoader<T : Any> {
  /** Source from which the [Image] will be obtained. */
  val source: T

  /**
   * Provides an [ImageLoader] through [provide].
   *
   * @param T Source from which the [Image] will be obtained.
   */
  fun interface Provider<T : Any> {
    /**
     * Provides an [ImageLoader].
     *
     * @param source Source from which the [Image] will be obtained.
     */
    fun provide(source: T): ImageLoader<T>

    companion object
  }

  /**
   * Loads an [Image].
   *
   * @param width How wide the [Image] is.
   * @param height How tall the [Image] is.
   */
  suspend fun load(width: Int, height: Int): Image?

  companion object
}

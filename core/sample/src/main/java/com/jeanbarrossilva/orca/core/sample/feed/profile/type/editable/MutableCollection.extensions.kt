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

package com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable

/**
 * Returns a [List] containing exactly the same elements of this [Collection], except for the ones
 * that match the [predicate] and have been replaced by the result of [replacement].
 *
 * @param replacement Lambda that receives the candidate being currently iterated and returns the
 *   replacement of the one that matches the [predicate].
 * @param predicate Indicates whether the given candidate should be replaced by the result of
 *   [replacement].
 * @return Whether an element matching the [predicate] has been found and replaced.
 * @throws IllegalStateException If multiple elements match the [predicate].
 */
fun <T> MutableList<T>.replaceOnceBy(replacement: T.() -> T, predicate: (T) -> Boolean): Boolean {
  var replaced: T? = null
  replaceBy(replacement) {
    val isMatch = predicate(it)
    if (isMatch) {
      if (replaced == null) {
        replaced = it
      } else {
        throw IllegalStateException("Multiple predicate matches: $replaced and $it.")
      }
    }
    isMatch
  }
  return replaced != null
}

/**
 * Replaces the elements that match the [predicate] by the result of [replacement].
 *
 * @param replacement Lambda that receives the candidate being currently iterated and returns the
 *   replacement of the one that matches the [predicate].
 * @param predicate Indicates whether the given candidate should be replaced by the result of
 *   [replacement].
 */
internal fun <T> MutableList<T>.replaceBy(replacement: T.() -> T, predicate: (T) -> Boolean) {
  replaceAll { if (predicate(it)) it.replacement() else it }
}

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

package com.jeanbarrossilva.orca.std.styledstring

/**
 * Applies the given [transform] to the currently iterated element if its [predicate] returns
 * `true`.
 *
 * @param I Element of the [Iterable] being conditionally mapped.
 * @param O Element of the resulting [List].
 * @param predicate Returns whether the element should be transformed.
 * @param transform Transformation to be performed on the element.
 */
internal fun <I, O : I> Iterable<I>.map(predicate: (I) -> Boolean, transform: (I) -> O): List<I> {
  return map { if (predicate(it)) transform(it) else it }
}

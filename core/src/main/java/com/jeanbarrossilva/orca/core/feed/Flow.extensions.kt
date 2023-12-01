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

package com.jeanbarrossilva.orca.core.feed

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Filters each element of the [Collection]s emitted to this [Flow].
 *
 * @param predicate Whether the currently iterated element should be in the filtered [List].
 */
internal fun <T> Flow<Collection<T>>.filterEach(predicate: suspend (T) -> Boolean): Flow<List<T>> {
  return map { elements -> elements.filter { element -> predicate(element) } }
}

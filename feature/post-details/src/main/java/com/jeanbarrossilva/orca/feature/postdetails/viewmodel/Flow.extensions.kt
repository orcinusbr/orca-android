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

package com.jeanbarrossilva.orca.feature.postdetails.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

/**
 * Creates a [Flow] that [combine]s both given [Flow]s and collects [transform]'s resulting one.
 *
 * @param firstFlow [Flow] to be [combine]d with [secondFlow].
 * @param secondFlow [Flow] to be [combine]d with [firstFlow].
 * @param transform Creates a [Flow] based on [firstFlow]'s and [secondFlow]'s emissions.
 */
internal fun <F, S, O> flatMapCombine(
  firstFlow: Flow<F>,
  secondFlow: Flow<S>,
  transform: suspend (F, S) -> Flow<O>
): Flow<O> {
  return flow {
    combine(firstFlow, secondFlow) { first, second -> transform(first, second) }.collect(::emitAll)
  }
}

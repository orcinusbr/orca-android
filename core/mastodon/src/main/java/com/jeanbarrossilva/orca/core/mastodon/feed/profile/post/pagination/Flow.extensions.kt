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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination

import java.util.Optional
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Associates each emission made to this [Flow] to its respective [association] result.
 *
 * @param association Returns the value to be associated to the one emitted to this [Flow].
 */
internal fun <I, O> Flow<I>.associateWith(association: (I) -> O): Flow<Pair<O, I>> {
  return map { association(it) to it }
}

/**
 * Transforms the emitted values that match their respective [predicate].
 *
 * @param I Value emitted to this [Flow].
 * @param predicate Returns whether the given value should be transformed.
 * @param transform Transformation to be performed on the currently iterated value.
 */
internal fun <I> Flow<I>.map(predicate: (I) -> Boolean, transform: (I) -> I): Flow<I> {
  return map { if (predicate(it)) transform(it) else it }
}

/**
 * Transforms the previous and the currently emitted value into a new one and returns a [Flow] to
 * which the non-`null` results are emitted.
 *
 * @param I Value emitted to this [Flow].
 * @param O Transformed value to be emitted to the resulting [Flow].
 * @param comparison Transformation to be performed on both the previous and the current values
 *   emitted to this [Flow].
 */
internal fun <I, O> Flow<I>.compareNotNull(
  comparison: (previous: Optional<I & Any>, current: I) -> O
): Flow<O & Any> {
  var previous = Optional.empty<I>()
  return flow {
    collect { current ->
      comparison(previous, current)?.let { transformed ->
        emit(transformed)
        current?.let { previous = Optional.of(it) }
      }
    }
  }
}

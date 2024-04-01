/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
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

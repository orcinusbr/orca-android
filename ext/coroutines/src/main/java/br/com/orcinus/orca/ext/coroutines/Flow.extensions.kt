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

package br.com.orcinus.orca.ext.coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

/**
 * Suspends until the next value is emitted to this [Flow] and then returns it.
 *
 * @param T Value emitted to this [Flow].
 */
suspend fun <T> Flow<T>.await(): T {
  return if (this is StateFlow<T>) {
    value.let { previous -> first { current -> current != previous } }
  } else {
    first()
  }
}

/**
 * Maps each element of the emitted [Collection]s to the resulting [Flow] of [transform], merging
 * and folding them into an up-to-date [List] that gets emitted each time any of these [Flow]s
 * receive an emission.
 *
 * @param transform Transformation to be made to the currently iterated element.
 */
fun <I, O> Flow<Collection<I>>.flatMapEach(transform: suspend (I) -> Flow<O>): Flow<List<O>> {
  return flatMapEach(key = { it }, transform)
}

/**
 * Maps each element of the emitted [Collection]s to the resulting [Flow] of [transform], merging
 * and folding them into an up-to-date [List] that gets emitted each time any of these [Flow]s
 * receive an emission.
 *
 * @param key Provides the value by which each element should be compared when replaced.
 * @param transform Transformation to be made to the currently iterated element.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <I, O, K> Flow<Collection<I>>.flatMapEach(
  key: (O) -> K,
  transform: suspend (I) -> Flow<O>
): Flow<List<O>> {
  val accumulator = LinkedHashMap<K, O>()
  return mapEach { elements ->
      transform(elements)
        .onEach { element -> accumulator[key(element)] = element }
        .map { accumulator }
    }
    .map(List<Flow<LinkedHashMap<K, O>>>::merge)
    .flattenMerge()
    .map { it.values.toList() }
    .distinctUntilChanged()
}

/**
 * Maps each element of the emitted [Collection]s to the result of [transform].
 *
 * @param transform Transformation to be made to the currently iterated element.
 */
fun <I, O> Flow<Collection<I>>.mapEach(transform: suspend (I) -> O): Flow<List<O>> {
  return map { elements -> elements.map { element -> transform(element) } }
}

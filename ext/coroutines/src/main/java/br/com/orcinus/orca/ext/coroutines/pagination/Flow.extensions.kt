/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.ext.coroutines.pagination

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.runningReduce

/**
 * Combines index-based [Flow]s of [Iterable]s into one.
 *
 * @param T Contained element to be appended when pagination is performed.
 * @param pagination Returns a [Flow] that emits values related to the given index, which will be
 *   appended to the ones that have been given for previous indices, and, finally, emitted to the
 *   resulting [Flow].
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<Int>.paginate(pagination: suspend (index: Int) -> Flow<Iterable<T>>): Flow<List<T>> {
  @Suppress("UNCHECKED_CAST")
  return distinctUntilChanged().flatMapMerge(transform = pagination).runningReduce {
    accumulator,
    posts ->
    accumulator + posts
  } as Flow<List<T>>
}

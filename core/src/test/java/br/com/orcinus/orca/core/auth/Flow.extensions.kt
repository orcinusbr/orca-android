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

package br.com.orcinus.orca.core.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

/**
 * Similarly to [Iterable.windowed], returns a [Flow] containing elements emitted to the receiver
 * one joined in groups whose sizes are [size]. Because the intended behavior is very specific,
 * partial windows aren't allowed (meaning that all emitted [List]s will have the exact given amount
 * of elements and the resulting [Flow] won't emit until they do).
 *
 * @param size Quantity of elements that should be emitted to the original [Flow] in order for the
 *   produced one to emit the snapshots.
 * @throws IllegalStateException If the [size] is negative.
 */
@Throws(IllegalStateException::class)
internal fun <T> Flow<T>.windowed(size: Int): Flow<List<T>> {
  require(size >= 0) { "Window size should be positive." }
  return when (size) {
    0 -> emptyFlow()
    1 -> map(::listOf)
    else -> {
      val window = ArrayList<T>(size)
      transform {
        val nextWindowSize = window.size.inc()
        if (nextWindowSize < size) {
          window += it
        } else if (nextWindowSize == size) {
          window += it
          emit(window.toList())
          window.clear()
        }
      }
    }
  }
}

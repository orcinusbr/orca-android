package com.jeanbarrossilva.orca.feature.feed.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

/**
 * Suspends until the next value is emitted to this [Flow] and then returns it.
 *
 * @param T Value emitted to this [Flow].
 */
internal suspend fun <T> Flow<T>.await(): T {
  return if (this is StateFlow<T>) {
    value.let { previous -> first { current -> current != previous } }
  } else {
    first()
  }
}

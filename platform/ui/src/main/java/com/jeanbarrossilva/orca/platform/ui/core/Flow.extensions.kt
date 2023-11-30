package com.jeanbarrossilva.orca.platform.ui.core

import com.jeanbarrossilva.orca.platform.ui.core.replacement.emptyReplacementList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.runningFold

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
  return flatMapEach(selector = { it }, transform)
}

/**
 * Maps each element of the emitted [Collection]s to the resulting [Flow] of [transform], merging
 * and folding them into an up-to-date [List] that gets emitted each time any of these [Flow]s
 * receive an emission.
 *
 * @param selector Returns the value by which elements should be compared when replacing them.
 * @param transform Transformation to be made to the currently iterated element.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <I, O, S> Flow<Collection<I>>.flatMapEach(
  selector: (O) -> S,
  transform: suspend (I) -> Flow<O>
): Flow<List<O>> {
  return mapEach(transform).flatMapLatest { flows ->
    flows.merge().runningFold(emptyReplacementList(selector)) { accumulator, element ->
      accumulator.add(element)
      accumulator
    }
  }
}

/**
 * Maps each element of the emitted [Collection]s to the result of [transform].
 *
 * @param transform Transformation to be made to the currently iterated element.
 */
fun <I, O> Flow<Collection<I>>.mapEach(transform: suspend (I) -> O): Flow<List<O>> {
  return map { elements -> elements.map { element -> transform(element) } }
}

package com.jeanbarrossilva.orca.feature.tootdetails.viewmodel

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

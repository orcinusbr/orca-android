/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
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

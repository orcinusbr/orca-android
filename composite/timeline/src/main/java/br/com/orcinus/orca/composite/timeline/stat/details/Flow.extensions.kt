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

package br.com.orcinus.orca.composite.timeline.stat.details

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

/**
 * Creates a [Flow] that emits the value emitted to the dependant (receiver) one only after
 * [primary] has emitted, joining both values to a [Pair].
 *
 * @param D Value emitted by the dependant [Flow].
 * @param P Value emitted by the [Flow] on which this one depends.
 * @param primary [Flow] whose emissions will be followed by those of the dependant one.
 */
internal fun <D, P> Flow<D>.dependOn(primary: Flow<P>): Flow<Pair<P, D>> {
  @OptIn(ExperimentalCoroutinesApi::class) return primary.flatMapConcat { p -> map { d -> p to d } }
}

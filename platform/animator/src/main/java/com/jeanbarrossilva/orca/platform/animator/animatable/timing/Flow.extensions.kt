/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.platform.animator.animatable.timing

import com.jeanbarrossilva.orca.ext.coroutines.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot

/**
 * Awaits the next `false` [Boolean] to be emitted and returns it.
 *
 * @see Flow.await
 * @see Flow.awaitTrue
 */
internal suspend fun Flow<Boolean>.awaitFalse(): Boolean {
  return filterNot { it }.await()
}

/**
 * Awaits the next `true` [Boolean] to be emitted and returns it.
 *
 * @see Flow.await
 * @see Flow.awaitFalse
 */
internal suspend fun Flow<Boolean>.awaitTrue(): Boolean {
  return filter { it }.await()
}

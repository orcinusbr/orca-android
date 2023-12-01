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

package com.jeanbarrossilva.orca.core.mastodon.auth.authorization.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Converts this [Flow] into a [MutableStateFlow] that mirrors its emissions.
 *
 * @param scope [CoroutineScope] from which this [Flow] will be collected and its emissions will be
 *   sent to the created [MutableStateFlow].
 * @param initialValue [Value][MutableStateFlow.value] that's initially held by the
 *   [MutableStateFlow].
 */
@Suppress("KDocUnresolvedReference")
internal fun <T> Flow<T>.mutableStateIn(
  scope: CoroutineScope,
  initialValue: T
): MutableStateFlow<T> {
  return MutableStateFlow(initialValue).apply {
    scope.launch { this@mutableStateIn.collect(this@apply) }
  }
}

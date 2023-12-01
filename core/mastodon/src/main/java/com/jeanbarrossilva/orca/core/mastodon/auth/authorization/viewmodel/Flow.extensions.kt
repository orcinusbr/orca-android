/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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

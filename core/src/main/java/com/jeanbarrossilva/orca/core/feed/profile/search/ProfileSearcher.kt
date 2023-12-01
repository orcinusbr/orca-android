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

package com.jeanbarrossilva.orca.core.feed.profile.search

import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf

/** Searches for profiles through [search]. */
abstract class ProfileSearcher {
  /** [MutableStateFlow] to which the query is emitted. */
  private val queryFlow = MutableStateFlow("")

  /** [Flow] with an empty [List] of [ProfileSearchResult]s. */
  private val emptyResultsFlow = flowOf(emptyList<ProfileSearchResult>())

  /**
   * [MutableStateFlow] to which [ProfileSearchResult]s based on the query are emitted.
   *
   * @see queryFlow
   */
  @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
  private val resultsFlow =
    queryFlow.debounce(256.milliseconds).flatMapMerge {
      if (it.isBlank()) emptyResultsFlow else onSearch(it)
    }

  /**
   * Searches for profiles that match the [query].
   *
   * @param query Input about the profiles being searched.
   */
  fun search(query: String): Flow<List<ProfileSearchResult>> {
    queryFlow.value = query.trim()
    return resultsFlow
  }

  /**
   * Searches for profiles that match the [query].
   *
   * @param query Input about the profiles being searched.
   */
  protected abstract suspend fun onSearch(query: String): Flow<List<ProfileSearchResult>>
}

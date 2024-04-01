/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.core.feed.profile.search

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

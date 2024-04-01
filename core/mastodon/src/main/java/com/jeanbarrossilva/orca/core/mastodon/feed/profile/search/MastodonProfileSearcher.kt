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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.search

import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.flow.loadableFlow
import com.jeanbarrossilva.loadable.flow.unwrap
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearcher
import com.jeanbarrossilva.orca.platform.cache.Cache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * [ProfileSearcher] that searches for [ProfileSearchResult]s and caches them to prevent excessive
 * resource consumption.
 *
 * @param cache [Cache] by which the [ProfileSearchResult]s will be cached.
 */
class MastodonProfileSearcher
internal constructor(private val cache: Cache<List<ProfileSearchResult>>) : ProfileSearcher() {
  /**
   * [MutableStateFlow] to which searched [Loadable]s of [ProfileSearchResult] are emitted and that
   * is later unwrapped.
   *
   * @see unwrap
   */
  private val searchResultsFlow = loadableFlow<List<ProfileSearchResult>>()

  override suspend fun onSearch(query: String): Flow<List<ProfileSearchResult>> {
    val searchResults = cache.get(query)
    searchResultsFlow.value = Loadable.Loaded(searchResults)
    return searchResultsFlow.unwrap()
  }
}

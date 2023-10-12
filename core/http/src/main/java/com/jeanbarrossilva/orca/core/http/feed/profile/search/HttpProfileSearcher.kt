package com.jeanbarrossilva.orca.core.http.feed.profile.search

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
class HttpProfileSearcher
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

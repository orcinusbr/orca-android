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

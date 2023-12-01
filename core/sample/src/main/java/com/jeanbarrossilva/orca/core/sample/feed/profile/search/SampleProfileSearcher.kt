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

package com.jeanbarrossilva.orca.core.sample.feed.profile.search

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearcher
import com.jeanbarrossilva.orca.core.feed.profile.search.toProfileSearchResult
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * [ProfileSearcher] that searches through the sample [Profile]s.
 *
 * @param provider [SampleProfileProvider] by which [Profile]s will be provided.
 */
internal class SampleProfileSearcher(private val provider: SampleProfileProvider) :
  ProfileSearcher() {
  override suspend fun onSearch(query: String): Flow<List<ProfileSearchResult>> {
    return provider.profilesFlow.map { profiles ->
      profiles.map(Profile::toProfileSearchResult).filter { profile ->
        profile.account.toString().contains(query, ignoreCase = true) ||
          profile.name.contains(query, ignoreCase = true)
      }
    }
  }
}

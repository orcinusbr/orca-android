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

import com.jeanbarrossilva.orca.core.sample.test.feed.profile.search.sample
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

internal class ProfileSearcherTests {
  @Test
  fun `GIVEN a blank query WHEN searching THEN it returns no results`() {
    val searcher =
      object : ProfileSearcher() {
        override suspend fun onSearch(query: String): Flow<List<ProfileSearchResult>> {
          return flowOf(listOf(ProfileSearchResult.sample))
        }
      }
    runTest { assertContentEquals(emptyList(), searcher.search(" ").first()) }
  }
}

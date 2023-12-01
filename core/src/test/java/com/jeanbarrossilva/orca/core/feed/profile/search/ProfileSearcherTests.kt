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

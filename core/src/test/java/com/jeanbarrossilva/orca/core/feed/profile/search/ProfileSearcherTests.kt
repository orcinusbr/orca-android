package com.jeanbarrossilva.orca.core.feed.profile.search

import com.jeanbarrossilva.orca.core.sample.feed.profile.search.samples
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
          return flowOf(ProfileSearchResult.samples)
        }
      }
    runTest { assertContentEquals(emptyList(), searcher.search(" ").first()) }
  }
}

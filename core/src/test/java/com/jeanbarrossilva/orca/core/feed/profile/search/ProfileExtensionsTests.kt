package com.jeanbarrossilva.orca.core.feed.profile.search

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.search.sample
import com.jeanbarrossilva.orca.core.sample.rule.SampleCoreTestRule
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.Rule

internal class ProfileExtensionsTests {
  @get:Rule val sampleCoreRule = SampleCoreTestRule()

  @Test
  fun `GIVEN a profile WHEN converting it into a search result THEN it's converted`() {
    assertEquals(ProfileSearchResult.sample, Profile.sample.toProfileSearchResult())
  }
}

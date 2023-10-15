package com.jeanbarrossilva.orca.core.sample.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.sample.rule.SampleCoreTestRule
import com.jeanbarrossilva.orca.core.sample.test.assertTogglingEquals
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.junit.Rule

internal class SampleProfileTests {
  @get:Rule val sampleCoreRule = SampleCoreTestRule()

  @Test
  fun `GIVEN a public unfollowed profile WHEN toggling its follow status THEN it's followed`() {
    runTest { assertTogglingEquals(Follow.Public.following(), Follow.Public.unfollowed()) }
  }

  @Test
  fun `GIVEN a public followed profile WHEN toggling its follow status THEN it's not followed`() {
    runTest { assertTogglingEquals(Follow.Public.unfollowed(), Follow.Public.following()) }
  }

  @Test
  fun `GIVEN a private unfollowed profile WHEN toggling its follow status THEN it's requested`() {
    runTest { assertTogglingEquals(Follow.Private.requested(), Follow.Private.unfollowed()) }
  }

  @Test
  fun `GIVEN a private requested profile WHEN toggling its follow status THEN it's unfollowed`() {
    runTest { assertTogglingEquals(Follow.Private.unfollowed(), Follow.Private.requested()) }
  }

  @Test
  fun `GIVEN a private followed profile WHEN toggling its follow status THEN it's unfollowed`() {
    runTest { assertTogglingEquals(Follow.Private.unfollowed(), Follow.Private.following()) }
  }
}

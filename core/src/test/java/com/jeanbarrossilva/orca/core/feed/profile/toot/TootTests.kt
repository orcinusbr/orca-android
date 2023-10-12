package com.jeanbarrossilva.orca.core.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.test.TestToot
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class TootTests {
  @Test
  fun `GIVEN an unliked toot WHEN liking it THEN it's liked`() {
    val toot = TestToot()
    runTest {
      toot.favorite.disable()
      toot.favorite.enable()
      assertTrue(toot.favorite.isEnabled)
    }
  }

  @Test
  fun `GIVEN a liked toot WHEN unliking it THEN it isn't liked`() {
    val toot = TestToot()
    runTest {
      toot.favorite.enable()
      toot.favorite.disable()
      assertFalse(toot.favorite.isEnabled)
    }
  }

  @Test
  fun `GIVEN a non-reblogged toot WHEN reblogging it THEN it's reblogged`() {
    val toot = TestToot()
    runTest {
      toot.reblog.disable()
      toot.reblog.enable()
      assertTrue(toot.reblog.isEnabled)
    }
  }

  @Test
  fun `GIVEN a reblogged toot WHEN un-reblogging it THEN it isn't reblogged`() {
    val toot = TestToot()
    runTest {
      toot.reblog.enable()
      toot.reblog.disable()
      assertFalse(toot.reblog.isEnabled)
    }
  }
}

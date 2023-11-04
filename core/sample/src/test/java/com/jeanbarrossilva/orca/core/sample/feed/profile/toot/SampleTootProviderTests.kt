package com.jeanbarrossilva.orca.core.sample.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.toot.samples
import com.jeanbarrossilva.orca.core.sample.test.instance.SampleInstanceTestRule
import com.jeanbarrossilva.orca.core.sample.test.instance.sample
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule

internal class SampleTootProviderTests {
  @get:Rule val instanceRule = SampleInstanceTestRule()

  @Test
  fun `GIVEN all toot samples WHEN getting them by their IDs THEN they're returned`() {
    runTest {
      Toot.samples.forEach { assertEquals(it, Instance.sample.tootProvider.provide(it.id).first()) }
    }
  }
}

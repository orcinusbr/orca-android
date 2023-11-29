package com.jeanbarrossilva.orca.core.sample.feed.profile.post

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.samples
import com.jeanbarrossilva.orca.core.sample.test.instance.SampleInstanceTestRule
import com.jeanbarrossilva.orca.core.sample.test.instance.sample
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule

internal class SamplePostProviderTests {
  @get:Rule val instanceRule = SampleInstanceTestRule()

  @Test
  fun `GIVEN all post samples WHEN getting them by their IDs THEN they're returned`() {
    runTest {
      Post.samples.forEach { assertEquals(it, Instance.sample.postProvider.provide(it.id).first()) }
    }
  }
}

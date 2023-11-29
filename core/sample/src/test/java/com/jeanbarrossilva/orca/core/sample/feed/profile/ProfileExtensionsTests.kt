package com.jeanbarrossilva.orca.core.sample.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.sample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.samples
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.sample
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

internal class ProfileExtensionsTests {
  @Test
  fun `GIVEN a sample profile WHEN getting its posts THEN they are the sample ones`() {
    runTest {
      assertContentEquals(
        Post.samples.filter { it.author == Author.sample }.take(SampleProfile.POSTS_PER_PAGE),
        Profile.sample.getPosts(0).first()
      )
    }
  }
}

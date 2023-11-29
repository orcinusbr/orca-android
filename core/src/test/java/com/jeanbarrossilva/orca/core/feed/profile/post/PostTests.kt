package com.jeanbarrossilva.orca.core.feed.profile.post

import com.jeanbarrossilva.orca.core.feed.profile.post.test.TestPost
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class PostTests {
  @Test
  fun `GIVEN an unliked post WHEN liking it THEN it's liked`() {
    val post = TestPost()
    runTest {
      post.favorite.disable()
      post.favorite.enable()
      assertTrue(post.favorite.isEnabled)
    }
  }

  @Test
  fun `GIVEN a liked post WHEN unliking it THEN it isn't liked`() {
    val post = TestPost()
    runTest {
      post.favorite.enable()
      post.favorite.disable()
      assertFalse(post.favorite.isEnabled)
    }
  }

  @Test
  fun `GIVEN a post WHEN reposting it THEN it's reposted`() {
    val post = TestPost()
    runTest {
      post.repost.disable()
      post.repost.enable()
      assertTrue(post.repost.isEnabled)
    }
  }

  @Test
  fun `GIVEN a repost WHEN unreposting it THEN it isn't reposted`() {
    val post = TestPost()
    runTest {
      post.repost.enable()
      post.repost.disable()
      assertFalse(post.repost.isEnabled)
    }
  }
}

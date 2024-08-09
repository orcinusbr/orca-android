/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.feed.profile.post

import br.com.orcinus.orca.core.feed.profile.post.test.DelegatorPost
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class PostTests {
  private val delegate
    get() =
      SampleInstance.Builder.create(NoOpSampleImageLoader.Provider)
        .withDefaultProfiles()
        .withDefaultPosts()
        .build()
        .postProvider
        .provideOneCurrent()

  @Test
  fun `GIVEN an unliked post WHEN liking it THEN it's liked`() {
    val delegator = DelegatorPost(delegate)
    runTest {
      delegator.favorite.disable()
      delegator.favorite.enable()
      assertTrue(delegator.favorite.isEnabled)
    }
  }

  @Test
  fun `GIVEN a liked post WHEN unliking it THEN it isn't liked`() {
    val delegator = DelegatorPost(delegate)
    runTest {
      delegator.favorite.enable()
      delegator.favorite.disable()
      assertFalse(delegator.favorite.isEnabled)
    }
  }

  @Test
  fun `GIVEN a post WHEN reposting it THEN it's reposted`() {
    val delegator = DelegatorPost(delegate)
    runTest {
      delegator.repost.disable()
      delegator.repost.enable()
      assertTrue(delegator.repost.isEnabled)
    }
  }

  @Test
  fun `GIVEN a repost WHEN unreposting it THEN it isn't reposted`() {
    val delegator = DelegatorPost(delegate)
    runTest {
      delegator.repost.enable()
      delegator.repost.disable()
      assertFalse(delegator.repost.isEnabled)
    }
  }
}

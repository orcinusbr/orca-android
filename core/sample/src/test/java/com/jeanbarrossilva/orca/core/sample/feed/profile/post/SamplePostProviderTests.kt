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

package com.jeanbarrossilva.orca.core.sample.feed.profile.post

import assertk.assertThat
import assertk.assertions.isEmpty
import com.jeanbarrossilva.orca.core.feed.profile.post.DeletablePost
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.samples
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader
import com.jeanbarrossilva.orca.core.sample.test.instance.SampleInstanceTestRule
import com.jeanbarrossilva.orca.core.sample.test.instance.sample
import com.jeanbarrossilva.testing.hasPropertiesEqualToThoseOf
import kotlin.test.Test
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule

internal class SamplePostProviderTests {
  @get:Rule val instanceRule = SampleInstanceTestRule(Instance.sample)

  @Test
  fun getsPostsByTheirIDs() {
    runTest {
      Post.samples.forEach {
        assertThat(Instance.sample.postProvider.provide(it.id).first())
          .hasPropertiesEqualToThoseOf(it)
      }
    }
  }

  @Test
  fun doesNotProvidePostWhenItIsDeleted() {
    assertThat(
        SamplePostWriter.provide { DeletablePost.createSample(TestSampleImageLoader.Provider, it) }
          .postProvider
          .apply {
            runTest {
              provide(sampleDeletablePostID).filterIsInstance<DeletablePost>().first().delete()
            }
          }
          .postsFlow
          .value
      )
      .isEmpty()
  }
}

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

package br.com.orcinus.orca.core.sample.feed.profile.post

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.core.feed.profile.post.DeletablePost
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.sample.test.feed.profile.post.withSamples
import br.com.orcinus.orca.core.sample.test.image.TestSampleImageLoader
import br.com.orcinus.orca.core.sample.test.instance.SampleInstanceTestRule
import br.com.orcinus.orca.core.sample.test.instance.sample
import br.com.orcinus.orca.ext.testing.hasPropertiesEqualToThoseOf
import kotlin.test.Test
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule

internal class SamplePostProviderTests {
  @get:Rule val instanceRule = SampleInstanceTestRule(Instance.sample)

  @Test
  fun providesAllPosts() {
    runTest {
      assertThat(Instance.sample.postProvider.provideAll().first())
        .isEqualTo(Instance.sample.postProvider.defaultPosts)
    }
  }

  @Test
  fun providesPostsByTheirIDs() {
    runTest {
      Posts.withSamples.forEach {
        assertThat(Instance.sample.postProvider.provide(it.id).first())
          .hasPropertiesEqualToThoseOf(it)
      }
    }
  }

  @Test
  fun doesNotProvidePostWhenItIsDeleted() {
    assertThat(
        Posts { add { DeletablePost.createSample(TestSampleImageLoader.Provider) } }
          .additionScope
          .writerProvider
          .provide()
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

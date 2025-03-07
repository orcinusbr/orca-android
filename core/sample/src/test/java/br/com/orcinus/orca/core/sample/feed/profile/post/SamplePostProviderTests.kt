/*
 * Copyright © 2023–2025 Orcinus
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

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.doesNotContain
import assertk.assertions.isNotEmpty
import br.com.orcinus.orca.core.feed.profile.post.OwnedPost
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class SamplePostProviderTests {
  @Test
  fun providesAllCurrentPosts() {
    val posts =
      SampleInstance.Builder.create(NoOpSampleImageLoader.Provider)
        .withDefaultProfiles()
        .withDefaultPosts()
        .build()
        .postProvider
        .provideAllCurrent()
    assertThat(posts).isNotEmpty()
  }

  @Test
  fun providesByID() {
    runTest {
      SampleInstance.Builder.create(NoOpSampleImageLoader.Provider)
        .withDefaultProfiles()
        .withDefaultPosts()
        .build()
        .postProvider
        .run {
          provideAllCurrent().forEach {
            provide(it.id).test(validate = TurbineTestContext<Post>::awaitItem)
          }
        }
    }
  }

  @Test
  fun doesNotProvideWhenPostIsDeleted() {
    runTest {
      val postProvider =
        SampleInstance.Builder.create(NoOpSampleImageLoader.Provider)
          .withDefaultProfiles()
          .withDefaultPosts()
          .build()
          .postProvider
      val deletedPost =
        postProvider.provideOneCurrent().own().apply {
          (this as OwnedPost).remove().getValueOrThrow()
        }
      val posts = postProvider.provideAllCurrent()
      assertThat(posts).doesNotContain(deletedPost)
    }
  }
}

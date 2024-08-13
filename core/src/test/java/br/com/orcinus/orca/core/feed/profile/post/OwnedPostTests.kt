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

import assertk.assertThat
import assertk.assertions.isTrue
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import br.com.orcinus.orca.ext.testing.hasPropertiesEqualToThoseOf
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class OwnedPostTests {
  @Test
  fun delegatesCommonCharacteristicsToDelegate() {
    val delegatePost =
      SampleInstance.Builder.create(NoOpSampleImageLoader.Provider)
        .withDefaultProfiles()
        .withDefaultPosts()
        .build()
        .postProvider
        .provideOneCurrent()
    val ownedPost =
      object : OwnedPost(delegatePost) {
        override suspend fun remove() {}
      }
    assertThat(ownedPost).hasPropertiesEqualToThoseOf(delegatePost)
  }

  @Test
  fun isDeleted() {
    runTest {
      val delegatePost =
        SampleInstance.Builder.create(NoOpSampleImageLoader.Provider)
          .withDefaultProfiles()
          .withDefaultPosts()
          .build()
          .postProvider
          .provideOneCurrent()
      var hasBeenDeleted = false
      object : OwnedPost(delegatePost) {
          override suspend fun remove() {
            hasBeenDeleted = true
          }
        }
        .remove()
      assertThat(hasBeenDeleted).isTrue()
    }
  }
}

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

package br.com.orcinus.orca.core.feed

import app.cash.turbine.test
import assertk.all
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import assertk.assertions.isInstanceOf
import assertk.assertions.prop
import assertk.coroutines.assertions.suspendCall
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.sample.feed.profile.post.content.SampleTermMuter
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import br.com.orcinus.orca.std.func.test.monad.isFailed
import br.com.orcinus.orca.std.func.test.monad.isSuccessful
import kotlin.test.Test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

internal class FeedProviderTests {
  @Test
  fun failsWhenProvidingFeedAtANegativePage() = runTest {
    assertThat(
        object : FeedProvider() {
          override val termMuter = SampleTermMuter()

          override suspend fun onProvision(page: Int) = emptyFlow<List<Post>>()
        }
      )
      .suspendCall("provide") { it.provide(page = -1) }
      .isFailed()
      .isInstanceOf<Pages.InvalidException>()
  }

  @Test
  fun provides() =
    assertThat(SampleInstance.Builder)
      .transform("create") { it.create(NoOpSampleImageLoader.Provider) }
      .prop(SampleInstance.Builder.Empty::withDefaultProfiles)
      .prop(SampleInstance.Builder.DefaultProfiles::withDefaultPosts)
      .prop(SampleInstance.Builder.DefaultPosts::build)
      .all {
        runTest {
          given { instance ->
            prop(SampleInstance::feedProvider)
              .suspendCall("provide") { feedProvider -> feedProvider.provide(page = 0) }
              .isSuccessful()
              .suspendCall("first", Flow<List<Post>>::first)
              .containsExactly(*instance.postProvider.provideAllCurrent().toTypedArray())
          }
        }
      }

  @Test
  fun filtersOutPostsContainingMutedTerms() = runTest {
    assertThat(SampleInstance.Builder)
      .transform("create") { it.create(NoOpSampleImageLoader.Provider) }
      .prop(SampleInstance.Builder.Empty::withDefaultProfiles)
      .prop(SampleInstance.Builder.DefaultProfiles::withDefaultPosts)
      .prop(SampleInstance.Builder.DefaultPosts::build)
      .apply {
        given { instance ->
          instance.postProvider
            .provideAllCurrent()
            .flatMap { post -> post.content.text.split(' ') }
            .toHashSet()
            .forEach { term -> instance.termMuter.mute(term) }
        }
      }
      .prop(SampleInstance::feedProvider)
      .suspendCall("provide") { it.provide(page = 0) }
      .isSuccessful()
      .suspendCall("test") { it.test { assertThat(awaitItem()).isEmpty() } }
  }
}

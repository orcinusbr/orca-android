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

package br.com.orcinus.orca.core.feed

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.sample.auth.SampleAuthenticationLock
import br.com.orcinus.orca.core.sample.auth.SampleAuthenticator
import br.com.orcinus.orca.core.sample.auth.actor.SampleActorProvider
import br.com.orcinus.orca.core.sample.feed.SampleFeedProvider
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileProvider
import br.com.orcinus.orca.core.sample.feed.profile.post.SamplePostProvider
import br.com.orcinus.orca.core.sample.feed.profile.post.content.SampleTermMuter
import br.com.orcinus.orca.core.sample.feed.profile.search.SampleProfileSearcher
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.core.sample.test.auth.actor.sample
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

internal class FeedProviderTests {
  @Test
  fun `GIVEN a nonexistent user's ID WHEN requesting a feed to be provided with it THEN it throws`() {
    val provider =
      object : FeedProvider() {
        override val termMuter = SampleTermMuter()

        override fun createNonexistentUserException(): NonexistentUserException {
          return NonexistentUserException(cause = null)
        }

        override suspend fun onProvide(userID: String, page: Int): Flow<List<Post>> {
          return emptyFlow()
        }

        override suspend fun containsUser(userID: String): Boolean {
          return false
        }
      }
    assertFailsWith<FeedProvider.NonexistentUserException> {
      runTest { provider.provide(userID = "🫨", page = 0) }
    }
  }

  @Test
  fun `GIVEN a negative page WHEN requesting a feed to be provided with it THEN it throws`() {
    val provider =
      object : FeedProvider() {
        override val termMuter = SampleTermMuter()

        override suspend fun containsUser(userID: String): Boolean {
          return true
        }

        override fun createNonexistentUserException(): NonexistentUserException {
          return NonexistentUserException(cause = null)
        }

        override suspend fun onProvide(userID: String, page: Int): Flow<List<Post>> {
          return emptyFlow()
        }
      }
    assertFailsWith<IndexOutOfBoundsException> {
      runTest { provider.provide(userID = "🥲", page = -1) }
    }
  }

  @Test
  fun `GIVEN a user ID WHEN requesting a feed to be provided with it THEN it's provided`() {
    runTest {
      val instance =
        SampleInstance.Builder.create(NoOpSampleImageLoader.Provider)
          .withDefaultProfiles()
          .withDefaultPosts()
          .build()
      val feed = instance.feedProvider.provide(Actor.Authenticated.sample.id, page = 0).first()
      val providedPosts = instance.postProvider.provideAllCurrent().toTypedArray()
      assertThat(feed).containsExactly(*providedPosts)
    }
  }

  @Test
  fun `GIVEN some muted terms WHEN providing a feed THEN posts with these terms are filtered out`() {
    val authenticator = SampleAuthenticator()
    val actorProvider = SampleActorProvider()
    val authenticationLock = SampleAuthenticationLock(authenticator, actorProvider)
    val profileProvider = SampleProfileProvider()
    val profileSearcher = SampleProfileSearcher(profileProvider)
    val postProvider = SamplePostProvider(profileProvider)
    val termMuter = SampleTermMuter()
    val imageLoaderProvider = NoOpSampleImageLoader.Provider
    val feedProvider =
      SampleFeedProvider(profileProvider, postProvider, termMuter, imageLoaderProvider)
    SampleInstance.Builder.create(
        authenticator,
        authenticationLock,
        feedProvider,
        profileProvider,
        profileSearcher,
        postProvider,
        imageLoaderProvider
      )
      .withDefaultProfiles()
      .withDefaultPosts()
    runTest {
      postProvider
        .provideAllCurrent()
        .flatMap { it.content.text.split(' ') }
        .toHashSet()
        .forEach { termMuter.mute(it) }
      feedProvider.provide(Actor.Authenticated.sample.id, page = 0).test {
        assertThat(awaitItem()).isEmpty()
      }
    }
  }
}

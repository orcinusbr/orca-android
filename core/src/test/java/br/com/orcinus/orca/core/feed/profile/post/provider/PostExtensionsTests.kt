/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.core.feed.profile.post.provider

import assertk.assertThat
import assertk.assertions.isSameAs
import assertk.assertions.isTrue
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.feed.profile.post.DeletablePost
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.sample.feed.profile.post.Posts
import br.com.orcinus.orca.core.sample.test.auth.actor.sample
import br.com.orcinus.orca.core.sample.test.feed.profile.post.withSample
import br.com.orcinus.orca.core.test.TestAuthenticationLock
import br.com.orcinus.orca.core.test.auth.Authenticator
import br.com.orcinus.orca.core.test.auth.actor.InMemoryActorProvider
import kotlin.test.Test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

internal class PostExtensionsTests {
  @Test
  fun schedulesAuthenticationUnlockWhenObtainingDeletableVersionOfPost() {
    var hasAuthenticationBeenScheduled = false
    val actorProvider = InMemoryActorProvider()
    val authenticator =
      Authenticator(actorProvider = actorProvider) {
        hasAuthenticationBeenScheduled = true
        Actor.Authenticated.sample
      }
    val authenticationLock = TestAuthenticationLock(actorProvider, authenticator)
    val post = Posts.withSample.single()
    runTest {
      object : PostProvider() {
          override val authenticationLock = authenticationLock

          override suspend fun onProvide(id: String): Flow<Post> {
            return flowOf(post)
          }
        }
        .provide(post.id)
        .first()
        .asDeletable(authenticationLock)
    }
    assertThat(hasAuthenticationBeenScheduled).isTrue()
  }

  @Test
  fun returnsItselfWhenMakingDeletablePostDeletable() {
    val authenticationLock = TestAuthenticationLock()
    val post =
      object : DeletablePost(Posts.withSample.single()) {
        override suspend fun delete() {}
      }
    runTest { assertThat(post.asDeletable(authenticationLock)).isSameAs(post) }
  }
}

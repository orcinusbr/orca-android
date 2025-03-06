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

package br.com.orcinus.orca.core.auth

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.auth.actor.FixedActorProvider
import br.com.orcinus.orca.core.sample.auth.SampleAuthenticator
import br.com.orcinus.orca.core.sample.test.auth.actor.sample
import br.com.orcinus.orca.core.test.auth.AuthenticationLock
import br.com.orcinus.orca.core.test.auth.Authenticator
import br.com.orcinus.orca.core.test.auth.AuthorizerBuilder
import br.com.orcinus.orca.core.test.auth.actor.InMemoryActorProvider
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest

internal class AuthenticationLockTests {
  @Test
  fun isNotifiedOfUnlock() {
    var isNotified = false
    val lock =
      object : AuthenticationLock<Authenticator>() {
        override val authorizer = AuthorizerBuilder().build()
        override val authenticator = SampleAuthenticator()
        override val actorProvider = InMemoryActorProvider()

        override suspend fun onUnlock(actor: Actor.Authenticated) {
          isNotified = true
        }

        override fun createFailedAuthenticationException(): FailedAuthenticationException {
          return FailedAuthenticationException(null)
        }
      }
    runTest { lock.scheduleUnlock {} }
    assertThat(isNotified, name = "isNotified").isTrue()
  }

  @Test
  fun isNotifiedOfSubsequentUnlock() {
    var notificationCount = 0
    val lock =
      object : AuthenticationLock<Authenticator>() {
        override val authorizer = AuthorizerBuilder().build()
        override val authenticator = SampleAuthenticator()
        override val actorProvider = InMemoryActorProvider()

        override suspend fun onUnlock(actor: Actor.Authenticated) {
          notificationCount++
        }

        override fun createFailedAuthenticationException(): FailedAuthenticationException {
          return FailedAuthenticationException(null)
        }
      }
    runTest {
      lock.scheduleUnlock {}
      lock.scheduleUnlock {}
    }
    assertThat(notificationCount, name = "notificationCount").isEqualTo(2)
  }

  @Test
  fun authenticatesWhenUnlockingWithUnauthenticatedActor() {
    val actorProvider = InMemoryActorProvider()
    var hasBeenAuthenticated = false
    val authorizer = AuthorizerBuilder().build()
    val authenticator =
      Authenticator(actorProvider) {
        hasBeenAuthenticated = true
        Actor.Authenticated.sample
      }
    runTest { AuthenticationLock(authorizer, authenticator, actorProvider).scheduleUnlock {} }
    assertThat(hasBeenAuthenticated).isTrue()
  }

  @Test
  fun unlocksWhenActorIsAuthenticated() {
    runTest {
      val actorProvider =
        InMemoryActorProvider().apply {
          (this as ActorProvider).remember(Actor.Authenticated.sample)
        }
      val authorizer = AuthorizerBuilder().build()
      var hasListenerBeenNotified = false
      AuthenticationLock(authorizer, actorProvider).scheduleUnlock {
        hasListenerBeenNotified = true
      }
      assertThat(hasListenerBeenNotified, name = "hasListenerBeenNotified").isTrue()
    }
  }

  @Test
  fun schedulesUnlocksForWhenOngoingOnesAreFinished() {
    runTest {
      val authorizer = AuthorizerBuilder().build()
      val actorProvider =
        InMemoryActorProvider().apply {
          (this as ActorProvider).remember(Actor.Authenticated.sample)
        }
      val lock = AuthenticationLock(authorizer, actorProvider)
      lock.scheduleUnlock { delay(32.seconds) }
      repeat(1_024) { lock.scheduleUnlock { delay(8.seconds) } }
      assertThat(
          @OptIn(ExperimentalCoroutinesApi::class)
          testScheduler.currentTime.milliseconds.inWholeSeconds
        )
        .isEqualTo(8_224)
    }
  }

  @Test
  fun unlocksWhileAnotherUnlockIsOngoing() {
    runTest {
      val actorProvider = FixedActorProvider(Actor.Authenticated.sample)
      val authorizer = AuthorizerBuilder().build()
      val lock = AuthenticationLock(authorizer, actorProvider)
      lock.scheduleUnlock {
        repeat(2_048) {
          launch(Dispatchers.Unconfined) {
            lock.scheduleUnlock {
              delay(1.nanoseconds)
              lock.scheduleUnlock {}
            }
          }
        }
      }
    }
  }
}

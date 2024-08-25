package br.com.orcinus.orca.core.auth

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.auth.actor.FixedActorProvider
import br.com.orcinus.orca.core.sample.test.auth.actor.sample
import br.com.orcinus.orca.core.test.auth.AuthenticationLock
import br.com.orcinus.orca.core.test.auth.Authenticator
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
  fun authenticatesWhenUnlockingWithUnauthenticatedActor() {
    val actorProvider = InMemoryActorProvider()
    var hasBeenAuthenticated = false
    val authenticator =
      Authenticator(actorProvider) {
        hasBeenAuthenticated = true
        Actor.Authenticated.sample
      }
    runTest { AuthenticationLock(authenticator, actorProvider).scheduleUnlock {} }
    assertThat(hasBeenAuthenticated).isTrue()
  }

  @Test
  fun unlocksWhenActorIsAuthenticated() {
    runTest {
      val actorProvider =
        InMemoryActorProvider().apply {
          (this as ActorProvider).remember(Actor.Authenticated.sample)
        }
      val authenticator = Authenticator(actorProvider = actorProvider)
      var hasListenerBeenNotified = false
      authenticator.authenticate()
      AuthenticationLock(authenticator, actorProvider).scheduleUnlock {
        hasListenerBeenNotified = true
      }
      assertThat(hasListenerBeenNotified).isTrue()
    }
  }

  @Test
  fun schedulesUnlocksForWhenOngoingOnesAreFinished() {
    runTest {
      val actorProvider =
        InMemoryActorProvider().apply {
          (this as ActorProvider).remember(Actor.Authenticated.sample)
        }
      val lock = AuthenticationLock(actorProvider)
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
      val authenticator = Authenticator(actorProvider)
      val lock = AuthenticationLock(authenticator, actorProvider)
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

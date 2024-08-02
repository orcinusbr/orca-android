package br.com.orcinus.orca.core.auth

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.FixedActorProvider
import br.com.orcinus.orca.core.sample.test.auth.actor.sample
import br.com.orcinus.orca.core.test.TestActorProvider
import br.com.orcinus.orca.core.test.TestAuthenticationLock
import br.com.orcinus.orca.core.test.TestAuthenticator
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest

internal class AuthenticationLockTests {
  @Test
  fun authenticatesWhenUnlockingWithUnauthenticatedActor() {
    var hasBeenAuthenticated = false
    val authenticator = TestAuthenticator { hasBeenAuthenticated = true }
    runTest { TestAuthenticationLock(authenticator = authenticator).scheduleUnlock {} }
    assertThat(hasBeenAuthenticated).isTrue()
  }

  @Test
  fun unlocksWhenActorIsAuthenticated() {
    val actorProvider = TestActorProvider()
    val authenticator = TestAuthenticator(actorProvider = actorProvider)
    var hasListenerBeenNotified = false
    runTest {
      authenticator.authenticate()
      TestAuthenticationLock(actorProvider, authenticator).scheduleUnlock {
        hasListenerBeenNotified = true
      }
    }
    assertThat(hasListenerBeenNotified).isTrue()
  }

  @Test
  fun schedulesUnlocksForWhenOngoingOnesAreFinished() {
    val lock = TestAuthenticationLock()
    runTest {
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
      val authenticator = TestAuthenticator(actorProvider = actorProvider)
      val lock = TestAuthenticationLock(actorProvider, authenticator)
      val scheduleCount = 2
      flow {
          var initialSchedulingContinuation: Continuation<Unit>? = null
          val initialSchedulingSuspensionJob =
            launch(Dispatchers.Unconfined, CoroutineStart.LAZY) {
              suspendCoroutine { initialSchedulingContinuation = it }
            }
          repeat(scheduleCount) { _ ->
            emit(
              async {
                lock.scheduleUnlock {
                  initialSchedulingContinuation?.resume(Unit)
                    ?: initialSchedulingSuspensionJob.takeUnless(Job::isActive)?.start()
                  it
                }
              }
            )
          }
        }
        .windowed(scheduleCount)
        .single()
        .awaitAll()
    }
  }
}

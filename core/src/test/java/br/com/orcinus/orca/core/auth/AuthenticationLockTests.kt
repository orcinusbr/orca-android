package br.com.orcinus.orca.core.auth

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import br.com.orcinus.orca.core.test.TestActorProvider
import br.com.orcinus.orca.core.test.TestAuthenticationLock
import br.com.orcinus.orca.core.test.TestAuthenticator
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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
}

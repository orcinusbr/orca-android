/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.core.mastodon.auth

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import br.com.orcinus.orca.core.mastodon.auth.authentication.MastodonAuthenticator
import br.com.orcinus.orca.core.mastodon.auth.authorization.MastodonAuthorizer
import br.com.orcinus.orca.core.mastodon.notification.NotificationLockBuilder
import br.com.orcinus.orca.core.sample.auth.actor.SampleActorProvider
import br.com.orcinus.orca.platform.testing.context
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class MastodonAuthenticationLockTests {
  private val actorProvider = SampleActorProvider()
  private val authorizer = MastodonAuthorizer(context)
  private val authenticator = MastodonAuthenticator(context, actorProvider)

  @Test
  fun unlocksNotificationLock() {
    var hasUnlocked = false
    val notificationLock = NotificationLockBuilder().onUnlock { hasUnlocked = true }.build(context)
    runTest {
      MastodonAuthenticationLock(
          context,
          notificationLock,
          authorizer,
          authenticator,
          actorProvider
        )
        .scheduleUnlock {}
    }
    assertThat(hasUnlocked, name = "hasUnlocked").isTrue()
    notificationLock.close()
  }

  @Test
  fun unlocksNotificationLockOnlyOnceAfterBeingUnlockedTwice() {
    var unlockCount = 0
    val notificationLock = NotificationLockBuilder().onUnlock { unlockCount++ }.build(context)
    runTest {
      MastodonAuthenticationLock(
          context,
          notificationLock,
          authorizer,
          authenticator,
          actorProvider
        )
        .apply { repeat(2) { scheduleUnlock {} } }
    }
    assertThat(unlockCount, name = "unlockCount").isEqualTo(1)
    notificationLock.close()
  }
}

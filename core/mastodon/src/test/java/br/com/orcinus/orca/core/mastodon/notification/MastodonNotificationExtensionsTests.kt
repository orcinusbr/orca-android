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

package br.com.orcinus.orca.core.mastodon.notification

import android.app.Notification
import assertk.all
import assertk.assertThat
import assertk.assertions.each
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import br.com.orcinus.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import br.com.orcinus.orca.core.sample.auth.actor.sample
import br.com.orcinus.orca.core.test.auth.AuthenticationLock
import br.com.orcinus.orca.platform.testing.context
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class MastodonNotificationExtensionsTests {
  @Test
  fun convertsIntoNotification() {
    val authenticationLock = AuthenticationLock(ActorProvider.sample)
    val createdAtAsZonedDateTime = ZonedDateTime.now()
    val createdAt = createdAtAsZonedDateTime.format(DateTimeFormatter.ISO_INSTANT)
    runTest {
      assertThat(MastodonNotification.Type.entries, "types").each { typeAssert ->
        typeAssert.given { type ->
          val mastodonNotification =
            MastodonNotification(
              /* id = */ "",
              type,
              createdAt,
              MastodonAccount.default,
              MastodonStatus.default
            )
          assertThat(mastodonNotification)
            .transform("toNotification") {
              runBlocking { mastodonNotification.toNotification(context, authenticationLock) }
            }
            .all {
              prop(Notification::extras)
                .transform("getString(Notification.EXTRA_TITLE)") { notification ->
                  notification.getString(Notification.EXTRA_TITLE)
                }
                .isEqualTo(
                  type.getContentTitleAsync(context, authenticationLock, mastodonNotification).get()
                )
              prop(Notification::`when`)
                .isEqualTo(createdAtAsZonedDateTime.toInstant().toEpochMilli())
            }
        }
      }
    }
  }
}

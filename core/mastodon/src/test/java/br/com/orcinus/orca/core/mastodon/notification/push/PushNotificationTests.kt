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

package br.com.orcinus.orca.core.mastodon.notification.push

import android.app.Notification
import assertk.all
import assertk.assertThat
import assertk.assertions.each
import assertk.assertions.isEqualTo
import assertk.assertions.isNotZero
import assertk.assertions.isSameInstanceAs
import assertk.assertions.isTrue
import assertk.assertions.prop
import assertk.coroutines.assertions.suspendCall
import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import br.com.orcinus.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import br.com.orcinus.orca.core.sample.auth.SampleAuthenticationLock
import br.com.orcinus.orca.core.sample.auth.SampleAuthenticator
import br.com.orcinus.orca.core.sample.auth.SampleAuthorizer
import br.com.orcinus.orca.core.sample.auth.actor.SampleActorProvider
import br.com.orcinus.orca.platform.testing.context
import java.time.ZonedDateTime
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class PushNotificationTests {
  private val authenticator = SampleAuthenticator()
  private val actorProvider = SampleActorProvider()
  private val authenticationLock =
    SampleAuthenticationLock(SampleAuthorizer, authenticator, actorProvider)
  private val createdAtAsZonedDateTime = ZonedDateTime.now()
  private val createdAt = PushNotification.createdAt(createdAtAsZonedDateTime)

  @Test
  fun convertsZonedDateTimeIntoCreatedAtString() =
    assertThat(PushNotification)
      .transform("createdAt") { it.createdAt(createdAtAsZonedDateTime) }
      .isEqualTo(createdAt)

  @Test
  fun generatedSystemNotificationIDIsHashCodeOfOriginalOneWhenItIsNotDigitOnly() =
    assertThat(
        PushNotification(
          /* id = */ "🪫",
          PushNotification.Type.FOLLOW,
          /* createdAt = */ "",
          MastodonAccount.default,
          /* status = */ null
        )
      )
      .prop(PushNotification::generateSystemNotificationID)
      .isEqualTo("🪫".hashCode())

  @Test
  fun generatedSystemNotificationIDIsHashCodeOfOriginalOneWhenItIsDigitOnlyButIsZeroPadded() =
    repeat(4) { index ->
      assertThat(
          PushNotification(
            /* id = */ "0".repeat(index.inc()) + "${index.inc()}",
            PushNotification.Type.FOLLOW,
            /* createdAt = */ "",
            MastodonAccount.default,
            /* status = */ null
          )
        )
        .prop(PushNotification::generateSystemNotificationID)
        .all { given { systemNotificationID -> isEqualTo(systemNotificationID.hashCode()) } }
    }

  @Test
  fun generatedSystemNotificationIDIsOriginalOneConvertedIntoAnIntegerWhenItIsDigitOnly() =
    assertThat(
        PushNotification(
          /* id = */ "8102024",
          PushNotification.Type.FOLLOW,
          /* createdAt = */ "",
          MastodonAccount.default,
          /* status = */ null
        )
      )
      .prop(PushNotification::generateSystemNotificationID)
      .isEqualTo(8102024)

  @Test
  fun getsContentTitle() =
    assertThat(PushNotification.Type.entries).each { typeAssert ->
      runTest {
        typeAssert.suspendCall("getContentTitle") { type ->
          type.getContentTitle(
            context,
            authenticationLock,
            MastodonAccount.default,
            MastodonStatus.default
          )
        }
      }
    }

  @Test
  fun notificationChannelIsThatOfItsType() =
    assertThat(PushNotification.Type.entries, "types").each { typeAssert ->
      typeAssert.given { type ->
        runTest {
          assertThat(
              PushNotification(
                /* id = */ "",
                type,
                createdAt,
                MastodonAccount.default,
                MastodonStatus.default
              )
            )
            .suspendCall("toNotification") { pushNotification ->
              pushNotification.toNotification(context, authenticationLock)
            }
            .prop(Notification::getChannelId)
            .isSameInstanceAs(type.channelID)
        }
      }
    }

  @Test
  fun notificationIsCancelledAutomatically() =
    assertThat(PushNotification.Type.entries, "types").each { typeAssert ->
      runTest {
        typeAssert.given { type ->
          assertThat(
              PushNotification(
                /* id = */ "",
                type,
                createdAt,
                MastodonAccount.default,
                MastodonStatus.default
              )
            )
            .suspendCall("toNotification") { pushNotification ->
              pushNotification.toNotification(context, authenticationLock)
            }
            .prop(Notification::flags)
            .prop("isAutoCancelled", Notification.FLAG_AUTO_CANCEL::and)
            .isNotZero()
        }
      }
    }

  @Test
  fun notificationTitleIsThatObtainableThroughItsType() =
    assertThat(PushNotification.Type.entries, "types").each { typeAssert ->
      typeAssert.given { type ->
        val pushNotification =
          PushNotification(
            /* id = */ "",
            type,
            createdAt,
            MastodonAccount.default,
            MastodonStatus.default
          )
        runTest {
          assertThat(pushNotification)
            .suspendCall("toNotification") { transformationDto ->
              transformationDto.toNotification(context, authenticationLock)
            }
            .prop(Notification::extras)
            .prop("title") { notification -> notification.getString(Notification.EXTRA_TITLE) }
            .isEqualTo(
              type.getContentTitle(
                context,
                authenticationLock,
                pushNotification.account,
                pushNotification.status
              )
            )
        }
      }
    }

  @Test
  fun notificationIsTimestamped() =
    assertThat(PushNotification.Type.entries, "types").each { typeAssert ->
      typeAssert.given { type ->
        runTest {
          assertThat(
              PushNotification(
                /* id = */ "",
                type,
                createdAt,
                MastodonAccount.default,
                MastodonStatus.default
              )
            )
            .suspendCall("toNotification") { pushNotification ->
              pushNotification.toNotification(context, authenticationLock)
            }
            .all {
              prop(Notification::extras)
                .prop("showWhen") { it.getBoolean(Notification.EXTRA_SHOW_WHEN) }
                .isTrue()
              prop(Notification::`when`)
                .isEqualTo(createdAtAsZonedDateTime.toInstant().toEpochMilli())
            }
        }
      }
    }
}

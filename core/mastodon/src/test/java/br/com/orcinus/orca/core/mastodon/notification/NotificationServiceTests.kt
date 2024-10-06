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
import android.content.Intent
import android.service.notification.StatusBarNotification
import app.cash.turbine.test
import assertk.all
import assertk.assertThat
import assertk.assertions.each
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import assertk.assertions.prop
import assertk.assertions.single
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.mastodon.BuildConfig
import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import br.com.orcinus.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.runAuthenticatedRequesterTest
import br.com.orcinus.orca.core.mastodon.instance.requester.runRequesterTest
import br.com.orcinus.orca.core.sample.auth.actor.sample
import br.com.orcinus.orca.core.test.auth.AuthenticationLock
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.injector.Injector
import com.google.firebase.messaging.RemoteMessage
import io.ktor.client.engine.mock.respondOk
import io.ktor.http.toURI
import java.net.URI
import java.time.ZonedDateTime
import kotlin.test.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ServiceController

@RunWith(RobolectricTestRunner::class)
internal class NotificationServiceTests {
  private val authenticationLock = AuthenticationLock(ActorProvider.sample)

  @Test
  fun instantiatingFromEmptyConstructorRetrievesInjectedRequester() {
    var isRetrieved = false
    runRequesterTest {
      Injector.injectLazily {
        isRetrieved = true
        requester
      }
      Injector.injectImmediately(authenticationLock)
    }
    NotificationService()
    assertThat(isRetrieved).isTrue()
    Injector.clear()
  }

  @Test
  fun instantiatingFromEmptyConstructorRetrievesInjectedAuthenticationLock() {
    var isRetrieved = false
    runRequesterTest {
      Injector.injectImmediately(requester)
      Injector.injectLazily {
        isRetrieved = true
        authenticationLock
      }
    }
    NotificationService()
    assertThat(isRetrieved).isTrue()
    Injector.clear()
  }

  @Test
  fun pushesSubscriptionWhenTokenIsReceived() {
    val requestURIFlow = MutableSharedFlow<URI>(replay = 1)
    runAuthenticatedRequesterTest(
      clientResponseProvider = {
        val requestURI = it.url.toURI()
        requestURIFlow.emit(requestURI)
        respondOk()
      },
      context = @OptIn(ExperimentalCoroutinesApi::class) UnconfinedTestDispatcher()
    ) {
      val service = NotificationService(requester, authenticationLock, coroutineContext)
      val intent = Intent(context, NotificationService::class.java)
      val controller =
        ServiceController.of(service, intent).apply(ServiceController<NotificationService>::bind)
      service.onNewToken("🤔🌼")
      requestURIFlow.test {
        assertThat(awaitItem())
          .isEqualTo(
            HostedURLBuilder.from(requester.baseURI).buildForNotificationSubscriptionPushing()
          )
      }
      controller.unbind()
    }
  }

  @Test
  fun sendsNotificationWhenMessageIsReceived() {
    val createdAt = MastodonNotification.createdAt(ZonedDateTime.now())
    runAuthenticatedRequesterTest(
      context = @OptIn(ExperimentalCoroutinesApi::class) UnconfinedTestDispatcher()
    ) {
      val service = NotificationService(requester, authenticationLock, coroutineContext)
      val intent = Intent(context, NotificationService::class.java)
      val controller = ServiceController.of(service, intent)
      assertThat(MastodonNotification.Type.entries).each { typeAssert ->
        typeAssert.given { type ->
          val mastodonNotification =
            MastodonNotification(
              /* id = */ "0",
              type,
              createdAt,
              MastodonAccount.default,
              MastodonStatus.default
            )
          val message =
            RemoteMessage.Builder(
                "${BuildConfig.mastodonfirebaseCloudMessagingSenderID}@fcm.googleapis.com"
              )
              .setMessageId("0")
              .setData(mastodonNotification.toMap())
              .build()
          controller.bind()
          service.onMessageReceived(message)
          controller.unbind()
          assertThat(service.manager.activeNotifications)
            .prop(Array<StatusBarNotification>::toSet)
            .single()
            .prop(StatusBarNotification::getNotification)
            .all {
              prop(Notification::getChannelId).isEqualTo(type.channelID)
              transform("extras.getString(Notification.EXTRA_TITLE)") {
                  it.extras.getString(Notification.EXTRA_TITLE)
                }
                .isEqualTo(
                  type.getContentTitleAsync(context, authenticationLock, mastodonNotification).get()
                )
            }
        }
      }
    }
  }
}

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
import android.app.NotificationManager
import android.service.notification.StatusBarNotification
import androidx.lifecycle.Lifecycle
import app.cash.turbine.test
import assertk.all
import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.each
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isSameInstanceAs
import assertk.assertions.isTrue
import assertk.assertions.prop
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.mastodon.BuildConfig
import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import br.com.orcinus.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import br.com.orcinus.orca.core.mastodon.instance.requester.runRequesterTest
import br.com.orcinus.orca.core.sample.auth.actor.sample
import br.com.orcinus.orca.core.test.auth.AuthenticationLock
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.injector.Injector
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.messaging.RemoteMessage
import io.ktor.client.engine.mock.respondOk
import io.ktor.http.toURI
import java.net.URI
import java.time.ZonedDateTime
import kotlin.test.Test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class MastodonNotificationServiceTests {
  private val messageBuilder = RemoteMessage.Builder(MESSAGE_RECIPIENT)
  private val dtoCreatedAt = MastodonNotification.createdAt(ZonedDateTime.now())

  @Test
  fun initialLifecycleStateIsInitializedOne() {
    runMastodonNotificationServiceTest {
      assertThat(controller.get())
        .prop(MastodonNotificationService::lifecycleState)
        .isSameInstanceAs(Lifecycle.State.INITIALIZED)
    }
  }

  @Test
  fun postCreationLifecycleStateIsCreatedOne() {
    runMastodonNotificationServiceTest {
      controller.create()
      assertThat(controller.get())
        .prop(MastodonNotificationService::lifecycleState)
        .isSameInstanceAs(Lifecycle.State.CREATED)
    }
  }

  @Test
  fun postDestructionLifecycleStateIsDestroyedOne() {
    runMastodonNotificationServiceTest {
      controller.create().bind().unbind().destroy()
      assertThat(controller.get())
        .prop(MastodonNotificationService::lifecycleState)
        .isSameInstanceAs(Lifecycle.State.DESTROYED)
    }
  }

  @Test
  fun instantiatingFromEmptyConstructorRetrievesInjectedRequester() {
    var isRetrieved = false
    runRequesterTest {
      Injector.injectLazily {
        isRetrieved = true
        requester
      }
      Injector.injectLazily { AuthenticationLock(ActorProvider.sample) }
    }
    MastodonNotificationService()
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
        AuthenticationLock(ActorProvider.sample)
      }
    }
    MastodonNotificationService()
    assertThat(isRetrieved).isTrue()
    Injector.clear()
  }

  @Test
  fun coroutineScopeIsActiveByDefault() {
    runMastodonNotificationServiceTest { controller.get().coroutineScope.ensureActive() }
  }

  @Test
  fun defaultFirebaseApplicationIsObtainableWhenSdkIsRunning() {
    runMastodonNotificationServiceTest {
      controller.create().bind()
      assertThat(controller.get()).prop(MastodonNotificationService::isFirebaseSdkRunning).isTrue()
      Firebase.app
    }
  }

  @Test
  fun defaultFirebaseApplicationThrowsWhenObtainedWhileSdkIsNotRunning() {
    runMastodonNotificationServiceTest {
      controller.create().bind().unbind().destroy()
      assertThat(controller.get()).prop(MastodonNotificationService::isFirebaseSdkRunning).isFalse()
      assertFailure { Firebase.app }.isInstanceOf<IllegalStateException>()
    }
  }

  @Test
  fun startsFirebaseSdkWhenCreated() {
    runMastodonNotificationServiceTest {
      controller.create().bind()
      assertThat(controller.get()).prop(MastodonNotificationService::isFirebaseSdkRunning).isTrue()
    }
  }

  @Test
  fun pushesSubscriptionWhenTokenIsReceived() {
    val requestURIFlow = MutableSharedFlow<URI>(replay = 1)
    runMastodonNotificationServiceTest({
      val requestURI = it.url.toURI()
      requestURIFlow.emit(requestURI)
      respondOk()
    }) {
      controller.create().bind().get().onNewToken("🤔🌼")
      requestURIFlow.test {
        assertThat(awaitItem())
          .isEqualTo(
            HostedURLBuilder.from(requester.baseURI).buildNotificationSubscriptionPushingRoute()
          )
      }
    }
  }

  @Test
  fun sendsNotificationWhenMessageIsReceived() {
    runMastodonNotificationServiceTest {
      controller.create()
      assertThat(MastodonNotification.Type.entries).each { typeAssert ->
        controller.bind()
        typeAssert.given { type ->
          val dto =
            MastodonNotification(
              /* id = */ "${type.ordinal}",
              type,
              dtoCreatedAt,
              MastodonAccount.default,
              MastodonStatus.default
            )
          val message = messageBuilder.setData(dto.toMap()).build()

          fun hasNotificationNotBeenSent(): Boolean {
            return controller
              .get()
              .notificationManager
              .activeNotifications
              .map(StatusBarNotification::getId)
              .contains(dto.normalizedID)
              .not()
          }

          controller.get().onMessageReceived(message)
          @Suppress("ControlFlowWithEmptyBody") while (hasNotificationNotBeenSent()) {}
          assertThat(controller.get())
            .prop(MastodonNotificationService::notificationManager)
            .prop(NotificationManager::getActiveNotifications)
            .transform("of $type") { statusBarNotifications ->
              statusBarNotifications.find { statusBarNotification ->
                statusBarNotification.id == dto.normalizedID
              }
            }
            .isNotNull()
            .prop(StatusBarNotification::getNotification)
            .all {
              prop(Notification::getChannelId).isEqualTo(type.channelID)
              transform("title") { it.extras.getString(Notification.EXTRA_TITLE) }
                .isEqualTo(type.getContentTitleAsync(context, requester.lock, dto).get())
            }
        }
        controller.unbind()
      }
    }
  }

  @Test
  fun cancelsSentNotificationsWhenDestroyed() {
    runMastodonNotificationServiceTest(
      coroutineContext = @OptIn(ExperimentalCoroutinesApi::class) UnconfinedTestDispatcher()
    ) {
      controller.create()
      for (type in MastodonNotification.Type.entries) {
        val data =
          MastodonNotification(
              /* id = */ "${type.ordinal}",
              type,
              dtoCreatedAt,
              MastodonAccount.default,
              MastodonStatus.default
            )
            .toMap()
        val message = messageBuilder.setData(data).build()
        controller.bind().get().onMessageReceived(message)
        controller.unbind()
      }
      controller.destroy()
      assertThat(controller.get())
        .prop(MastodonNotificationService::notificationManager)
        .prop(NotificationManager::getActiveNotifications)
        .isEmpty()
    }
  }

  @Test
  fun cancelsCoroutineScopeWhenDestroyed() {
    runMastodonNotificationServiceTest {
      controller.create().bind().unbind().destroy()
      assertThat(controller.get())
        .prop(MastodonNotificationService::coroutineScope)
        .prop(CoroutineScope::isActive)
        .isFalse()
    }
  }

  @Test
  fun stopsFirebaseSdkWhenDestroyed() {
    runMastodonNotificationServiceTest {
      controller.create().bind().unbind().destroy()
      assertThat(controller.get()).prop(MastodonNotificationService::isFirebaseSdkRunning).isFalse()
    }
  }

  companion object {
    private const val MESSAGE_RECIPIENT =
      "${BuildConfig.mastodonfirebaseCloudMessagingSenderID}@fcm.googleapis.com"
  }
}
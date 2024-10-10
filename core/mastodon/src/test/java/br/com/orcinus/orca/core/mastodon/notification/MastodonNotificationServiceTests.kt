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
import android.content.Intent
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
import br.com.orcinus.orca.core.mastodon.BuildConfig
import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import br.com.orcinus.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.runAuthenticatedRequesterTest
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ServiceController

@RunWith(RobolectricTestRunner::class)
internal class MastodonNotificationServiceTests {
  private val messageBuilder = RemoteMessage.Builder(MESSAGE_RECIPIENT)
  private val dtoCreatedAt = MastodonNotification.createdAt(ZonedDateTime.now())

  @Test
  fun initialLifecycleStateIsInitializedOne() {
    runMastodonNotificationServiceTest {
      assertThat(service)
        .prop(MastodonNotificationService::lifecycleState)
        .isSameInstanceAs(Lifecycle.State.INITIALIZED)
    }
  }

  @Test
  fun postCreationLifecycleStateIsCreatedOne() {
    runMastodonNotificationServiceTest {
      create()
      assertThat(service)
        .prop(MastodonNotificationService::lifecycleState)
        .isSameInstanceAs(Lifecycle.State.CREATED)
    }
  }

  @Test
  fun postDestructionLifecycleStateIsDestroyedOne() {
    runMastodonNotificationServiceTest {
      destroy()
      assertThat(service)
        .prop(MastodonNotificationService::lifecycleState)
        .isSameInstanceAs(Lifecycle.State.DESTROYED)
    }
  }

  @Test
  fun instantiatingFromEmptyConstructorRetrievesInjectedRequester() {
    var isRetrieved = false
    runAuthenticatedRequesterTest {
      Injector.injectLazily<Requester> {
        isRetrieved = true
        requester
      }
      MastodonNotificationService()
    }
    assertThat(isRetrieved).isTrue()
    Injector.clear()
  }

  @Test
  fun instantiatingFromEmptyConstructorRetrievesAuthenticationLockInjectedIntoRegisteredCoreModule() {
    var isRetrieved = false
    runAuthenticatedRequesterTest(
      onAuthentication = { isRetrieved = true },
      context = @OptIn(ExperimentalCoroutinesApi::class) UnconfinedTestDispatcher()
    ) {
      Injector.injectImmediately<Requester>(requester)
      MastodonNotificationService()
        .let { ServiceController.of(it, Intent(context, it::class.java)) }
        ?.create()
        ?.also {
          it.get()?.setCoroutineContext(coroutineContext)
          it.get()?.onNewToken("⌘")
        }
        ?.destroy()
    }
    assertThat(isRetrieved).isTrue()
    Injector.clear()
  }

  @Test
  fun coroutineScopeIsActiveByDefault() {
    runMastodonNotificationServiceTest { service.coroutineScope.ensureActive() }
  }

  @Test
  fun setsCoroutineContext() {
    runMastodonNotificationServiceTest {
      service.setCoroutineContext(Dispatchers.IO)
      assertThat(service)
        .prop(MastodonNotificationService::coroutineScope)
        .prop(CoroutineScope::coroutineContext)
        .transform("[${Dispatchers.IO.key}]") { it[Dispatchers.IO.key] }
        .isSameInstanceAs(Dispatchers.IO)
    }
  }

  @Test
  fun cancelsActiveJobsWhenSettingCoroutineContext() {
    runMastodonNotificationServiceTest {
      val job = service.coroutineScope.launch { awaitCancellation() }
      service.setCoroutineContext(Dispatchers.IO)
      assertThat(job).prop(Job::isCancelled).isTrue()
    }
  }

  @Test
  fun defaultFirebaseApplicationIsObtainableWhenSdkIsRunning() {
    runMastodonNotificationServiceTest {
      create()
      assertThat(service).prop(MastodonNotificationService::isFirebaseSdkRunning).isTrue()
      Firebase.app
    }
  }

  @Test
  fun defaultFirebaseApplicationThrowsWhenObtainedWhileSdkIsNotRunning() {
    runMastodonNotificationServiceTest {
      destroy()
      assertThat(service).prop(MastodonNotificationService::isFirebaseSdkRunning).isFalse()
      assertFailure { Firebase.app }.isInstanceOf<IllegalStateException>()
    }
  }

  @Test
  fun startsFirebaseSdkWhenCreated() {
    runMastodonNotificationServiceTest {
      create()
      assertThat(service).prop(MastodonNotificationService::isFirebaseSdkRunning).isTrue()
    }
  }

  @Test
  fun disablesFirebaseSdkAutomaticDataCollection() {
    runMastodonNotificationServiceTest {
      create()
      assertThat(Firebase)
        .prop("app") { it.app }
        .prop("isDataCollectionDefaultEnabled") { it.isDataCollectionDefaultEnabled }
        .isFalse()
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
      create()
      service.onNewToken("🤔🌼")
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
    runMastodonNotificationServiceTest(
      coroutineContext = @OptIn(ExperimentalCoroutinesApi::class) UnconfinedTestDispatcher()
    ) {
      create()
      assertThat(MastodonNotification.Type.entries).each { typeAssert ->
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

          service.onMessageReceived(message)
          service.waitUntilSent(dto.generateSystemNotificationID())
          assertThat(service)
            .prop(MastodonNotificationService::notificationManager)
            .prop(NotificationManager::getActiveNotifications)
            .transform("of $type") { statusBarNotifications ->
              statusBarNotifications.find { statusBarNotification ->
                statusBarNotification.id == dto.generateSystemNotificationID()
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
      }
    }
  }

  @Test
  fun cancelsSentNotificationsWhenDestroyed() {
    runMastodonNotificationServiceTest(
      coroutineContext = @OptIn(ExperimentalCoroutinesApi::class) UnconfinedTestDispatcher()
    ) {
      create()
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
        service.onMessageReceived(message)
      }
      destroy()
      assertThat(service)
        .prop(MastodonNotificationService::notificationManager)
        .prop(NotificationManager::getActiveNotifications)
        .isEmpty()
    }
  }

  @Test
  fun cancelsCoroutineScopeWhenDestroyed() {
    runMastodonNotificationServiceTest {
      destroy()
      assertThat(service)
        .prop(MastodonNotificationService::coroutineScope)
        .prop(CoroutineScope::isActive)
        .isFalse()
    }
  }

  @Test
  fun stopsFirebaseSdkWhenDestroyed() {
    runMastodonNotificationServiceTest {
      destroy()
      assertThat(service).prop(MastodonNotificationService::isFirebaseSdkRunning).isFalse()
    }
  }

  companion object {
    private const val MESSAGE_RECIPIENT =
      "${BuildConfig.mastodonfirebaseCloudMessagingSenderID}@fcm.googleapis.com"
  }
}

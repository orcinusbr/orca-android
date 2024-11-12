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

import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.service.notification.StatusBarNotification
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import assertk.all
import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.each
import assertk.assertions.exactly
import assertk.assertions.hasSize
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
import br.com.orcinus.orca.core.mastodon.instance.requester.ClientResponseProvider
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.runAuthenticatedRequesterTest
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.injector.Injector
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.messaging.RemoteMessage
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.ParametersBuilder
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
import org.robolectric.Shadows.shadowOf
import org.robolectric.android.controller.ServiceController
import org.robolectric.shadows.ShadowApplication

@RunWith(RobolectricTestRunner::class)
internal class NotificationServiceTests {
  private val message = RemoteMessage.Builder(MESSAGE_RECIPIENT).build()
  private val dtoCreatedAt = MastodonNotification.createdAt(ZonedDateTime.now())

  @Test
  fun initialLifecycleStateIsInitializedOne() = runNotificationServiceTest {
    assertThat(service)
      .prop(NotificationService::lifecycleState)
      .isSameInstanceAs(Lifecycle.State.INITIALIZED)
  }

  @Test
  fun postCreationLifecycleStateIsCreatedOne() = runNotificationServiceTest {
    create()
    assertThat(service)
      .prop(NotificationService::lifecycleState)
      .isSameInstanceAs(Lifecycle.State.CREATED)
  }

  @Test
  fun postDestructionLifecycleStateIsDestroyedOne() = runNotificationServiceTest {
    destroy()
    assertThat(service)
      .prop(NotificationService::lifecycleState)
      .isSameInstanceAs(Lifecycle.State.DESTROYED)
  }

  @Test
  fun instantiatingFromEmptyConstructorRetrievesInjectedRequester() {
    var isRetrieved = false
    runAuthenticatedRequesterTest {
      Injector.injectLazily<Requester> {
        isRetrieved = true
        requester
      }
      NotificationService()
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
      NotificationService()
        .let { ServiceController.of(it, Intent(context, it::class.java)) }
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
  fun binds() {
    val shadowApplication: ShadowApplication =
      shadowOf(ApplicationProvider.getApplicationContext<Application>())
    NotificationService.bind(context)
    assertThat(shadowApplication)
      .prop<_, List<Intent>, _>(ShadowApplication::getAllStartedServices)
      .exactly(1) {
        it
          .prop(Intent::getComponent)
          .isNotNull()
          .prop(ComponentName::getClassName)
          .isEqualTo(NotificationService::class.qualifiedName)
      }
    shadowApplication.clearStartedServices()
  }

  @Test
  fun bindsOnce() {
    val shadowApplication: ShadowApplication =
      shadowOf(ApplicationProvider.getApplicationContext<Application>())
    repeat(2) { NotificationService.bind(context) }
    assertThat(shadowApplication)
      .prop<_, List<ServiceConnection>, _>(ShadowApplication::getBoundServiceConnections)
      .hasSize(1)
    shadowApplication.clearStartedServices()
  }

  @Test
  fun coroutineScopeIsActiveByDefault() = runNotificationServiceTest {
    service.coroutineScope.ensureActive()
  }

  @Test
  fun setsCoroutineContext() = runNotificationServiceTest {
    service.setCoroutineContext(Dispatchers.IO)
    assertThat(service)
      .prop(NotificationService::coroutineScope)
      .prop(CoroutineScope::coroutineContext)
      .transform("[${Dispatchers.IO.key}]") { it[Dispatchers.IO.key] }
      .isSameInstanceAs(Dispatchers.IO)
  }

  @Test
  fun cancelsActiveJobsWhenSettingCoroutineContext() = runNotificationServiceTest {
    val job = service.coroutineScope.launch { awaitCancellation() }
    service.setCoroutineContext(Dispatchers.IO)
    assertThat(job).prop(Job::isCancelled).isTrue()
  }

  @Test
  fun defaultFirebaseApplicationIsObtainableWhenSdkIsRunning() = runNotificationServiceTest {
    create()
    assertThat(service).prop(NotificationService::isFirebaseSdkRunning).isTrue()
    Firebase.app
  }

  @Test
  fun defaultFirebaseApplicationThrowsWhenObtainedWhileSdkIsNotRunning() =
    runNotificationServiceTest {
      destroy()
      assertThat(service).prop(NotificationService::isFirebaseSdkRunning).isFalse()
      assertFailure { Firebase.app }.isInstanceOf<IllegalStateException>()
    }

  @Test
  fun startsFirebaseSdkWhenCreated() = runNotificationServiceTest {
    create()
    assertThat(service).prop(NotificationService::isFirebaseSdkRunning).isTrue()
  }

  @Test
  fun disablesFirebaseSdkAutomaticDataCollection() = runNotificationServiceTest {
    create()
    assertThat(Firebase)
      .prop("app") { it.app }
      .prop("isDataCollectionDefaultEnabled") { it.isDataCollectionDefaultEnabled }
      .isFalse()
  }

  @Test
  fun subscribesWhenTokenIsReceived() {
    val requestURIFlow = MutableSharedFlow<URI>(replay = 1)
    runNotificationServiceTest({
      val requestURI = it.url.toURI()
      requestURIFlow.emit(requestURI)
      respondOk()
    }) {
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
  fun subscriptionFormIsComplete() {
    val requestBodyFlow = MutableSharedFlow<FormDataContent>(replay = 1)
    runNotificationServiceTest({
      requestBodyFlow.emit(it.body as FormDataContent)
      respondOk()
    }) {
      service.onNewToken("🎈")
      requestBodyFlow.test {
        val requestBody = awaitItem()
        assertThat(requestBody)
          .prop(FormDataContent::formData)
          .isEqualTo(
            ParametersBuilder(requestBody.formData.entries().size)
              .apply {
                requester.lock.scheduleUnlock {
                  service.appendSubscriptionFormData(this, actorID = it.id, token = "🎈")
                }
              }
              .build()
          )
      }
    }
  }

  @Test
  fun sendsNotificationWhenMessageIsReceived() =
    runNotificationServiceTest(
      coroutineContext = @OptIn(ExperimentalCoroutinesApi::class) UnconfinedTestDispatcher()
    ) {
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
          clientResponseProvider.notification = dto
          service.onMessageReceived(message)
          launch { service.awaitUntilSent(dto.generateSystemNotificationID()) }
          assertThat(service)
            .prop(NotificationService::notificationManager)
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
              launch {
                transform("title") { it.extras.getString(Notification.EXTRA_TITLE) }
                  .isEqualTo(type.getContentTitle(context, requester.lock, dto))
              }
            }
        }
      }
    }

  @Test
  fun cancelsSentNotificationsWhenDestroyed() {
    lateinit var baseURI: URI
    runNotificationServiceTest(
      NotificationsClientResponseProvider({ baseURI }, ClientResponseProvider.ok),
      coroutineContext = @OptIn(ExperimentalCoroutinesApi::class) UnconfinedTestDispatcher()
    ) {
      baseURI = requester.baseURI
      for (type in MastodonNotification.Type.entries) {
        val dto =
          MastodonNotification(
            /* id = */ "${type.ordinal}",
            type,
            dtoCreatedAt,
            MastodonAccount.default,
            MastodonStatus.default
          )
        clientResponseProvider.notification = dto
        service.onMessageReceived(message)
        service.awaitUntilSent(dto.generateSystemNotificationID())
      }
      destroy()
      assertThat(service)
        .prop(NotificationService::notificationManager)
        .prop(NotificationManager::getActiveNotifications)
        .isEmpty()
    }
  }

  @Test
  fun cancelsCoroutineScopeWhenDestroyed() = runNotificationServiceTest {
    destroy()
    assertThat(service)
      .prop(NotificationService::coroutineScope)
      .prop(CoroutineScope::isActive)
      .isFalse()
  }

  @Test
  fun stopsFirebaseSdkWhenDestroyed() = runNotificationServiceTest {
    destroy()
    assertThat(service).prop(NotificationService::isFirebaseSdkRunning).isFalse()
  }

  companion object {
    private const val MESSAGE_RECIPIENT =
      "${BuildConfig.mastodonfirebaseCloudMessagingSenderID}@fcm.googleapis.com"
  }
}
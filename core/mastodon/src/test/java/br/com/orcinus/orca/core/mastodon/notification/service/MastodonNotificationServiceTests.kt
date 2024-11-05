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

package br.com.orcinus.orca.core.mastodon.notification.service

import android.app.Application
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.service.notification.StatusBarNotification
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.cause
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isSameInstanceAs
import assertk.assertions.isTrue
import assertk.assertions.prop
import assertk.assertions.single
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.runAuthenticatedRequesterTest
import br.com.orcinus.orca.core.mastodon.instance.requester.runRequesterTest
import br.com.orcinus.orca.core.mastodon.notification.service.MastodonNotificationService.Companion.endpoint
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.injector.Injector
import com.google.firebase.Firebase
import com.google.firebase.app
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.ParametersBuilder
import java.net.URI
import java.net.URISyntaxException
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
internal class MastodonNotificationServiceTests {
  @Test
  fun endpointIsPutIntoIntent() {
    assertThat(MastodonNotificationService)
      .transform("getIntent") {
        it.createIntent(context, MastodonNotificationServiceTestScope.endpoint)
      }
      .prop("endpoint") { it.endpoint }
      .isNotNull()
  }

  @Test
  fun getsEndpointFromIntent() {
    assertThat(MastodonNotificationService)
      .transform("getIntent") {
        it.createIntent(context, MastodonNotificationServiceTestScope.endpoint)
      }
      .prop("endpoint") { it.endpoint }
      .isEqualTo(MastodonNotificationServiceTestScope.endpoint)
  }

  @Test
  fun returnsNullWhenGettingEndpointNotPutIntoAnIntent() {
    assertThat(Intent(context, MastodonNotificationService::class.java))
      .prop("endpoint") { it.endpoint }
      .isNull()
  }

  @Test
  fun throwsWhenBindingWithoutAnEndpoint() {
    assertFailure {
        runRequesterTest {
          Injector.injectImmediately(requester)
          ServiceController.of(
              MastodonNotificationService(),
              Intent(context, MastodonNotificationService::class.java)
            )
            ?.bind()
        }
      }
      .isInstanceOf<IllegalStateException>()
    Injector.clear()
  }

  @Test
  fun throwsWhenBindingWithAnInvalidEndpoint() {
    runRequesterTest {
      Injector.injectImmediately(requester)
      assertFailure {
          ServiceController.of(
              MastodonNotificationService(),
              MastodonNotificationService.createIntent(
                context,
                MastodonNotificationServiceTestScope.endpoint
                  .toMutableList()
                  .apply { add(indices.random(), ' ') }
                  .joinToString(separator = "")
              )
            )
            ?.bind()
        }
        .isInstanceOf<RuntimeException>()
        .cause()
        .isNotNull()
        .isInstanceOf<URISyntaxException>()
    }
    Injector.clear()
  }

  @Test
  fun binds() {
    MastodonNotificationService.bind(
      context,
      MastodonNotificationServiceTestScope.createIntent(context)
    )
    assertThat<Application>(ApplicationProvider.getApplicationContext<Application>())
      .prop<_, ShadowApplication, _>(::shadowOf)
      .prop<_, List<Intent>, _>(ShadowApplication::getAllStartedServices)
      .single()
      .prop(Intent::getComponent)
      .isNotNull()
      .prop(ComponentName::getClassName)
      .isEqualTo(MastodonNotificationService::class.qualifiedName)
    Injector.clear()
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
      ServiceController.of(
          MastodonNotificationService(),
          Intent(context, MastodonNotificationService::class.java)
        )
        .also<ServiceController<MastodonNotificationService>> {
          it.get()?.setCoroutineContext(coroutineContext)
          it.create()
        }
        .destroy()
    }
    assertThat(isRetrieved).isTrue()
    Injector.clear()
  }

  @Test
  fun instantiatingFromEmptyConstructorInjectsOnMessageReceiptListener() {
    runRequesterTest {
      Injector.injectImmediately(requester)
      MastodonNotificationService()
      assertThat(Injector).transform("get") { it.get<OnMessageReceiptListener>() }
    }
    Injector.clear()
  }

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
  fun subscribesWhenCreated() {
    val requestBodyFlow = MutableSharedFlow<FormDataContent>(replay = 1)
    runMastodonNotificationServiceTest({
      requestBodyFlow.emit(it.body as FormDataContent)
      respondOk()
    }) {
      create()
      requestBodyFlow.test {
        val requestBody = awaitItem()
        assertThat(requestBody)
          .prop(FormDataContent::formData)
          .isEqualTo(
            ParametersBuilder(requestBody.formData.entries().size)
              .apply(service::appendSubscriptionFormData)
              .build()
          )
      }
    }
  }

  @Test
  fun onMessageReceiptListenerInjectedByEmptyConstructorSendsLastNotification() {
    lateinit var baseURI: URI
    val clientResponseProvider = MastodonNotificationsClientResponseProvider({ baseURI })
    val notificationID = clientResponseProvider.notification.generateSystemNotificationID()
    runAuthenticatedRequesterTest(clientResponseProvider) {
      baseURI = requester.baseURI
      Injector.injectImmediately<Requester>(requester)
      val service = MastodonNotificationService()
      val controller =
        ServiceController.of(
            service,
            MastodonNotificationService.createIntent(
              context,
              MastodonNotificationServiceTestScope.endpoint
            )
          )
          .apply<ServiceController<MastodonNotificationService>>(
            ServiceController<MastodonNotificationService>::create
          )
      Injector.get<OnMessageReceiptListener>()()
      service.awaitUntilSent(notificationID)
      assertThat(controller)
        .prop<_, MastodonNotificationService, _>(
          ServiceController<MastodonNotificationService>::get
        )
        .prop(MastodonNotificationService::notificationManager)
        .prop<_, Array<StatusBarNotification>, _>(NotificationManager::getActiveNotifications)
        .prop(Array<StatusBarNotification>::last)
        .prop(StatusBarNotification::getId)
        .isEqualTo(notificationID)
      controller.destroy()
    }
    Injector.clear()
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
}

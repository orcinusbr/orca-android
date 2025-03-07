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

import androidx.lifecycle.Lifecycle
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isSameInstanceAs
import assertk.assertions.prop
import assertk.coroutines.assertions.suspendCall
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import br.com.orcinus.orca.std.func.test.monad.isSuccessful
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.job
import kotlinx.serialization.json.Json
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class PushPushNotificationServiceTestScopeTests {
  @Test
  fun runsBodyOnce() {
    var runCount = 0
    runPushNotificationServiceTest { runCount++ }
    assertThat(runCount).isEqualTo(1)
  }

  @Test
  fun serviceIsInitializedByDefault() {
    runPushNotificationServiceTest {
      assertThat(service)
        .prop(PushNotificationService::lifecycleState)
        .isSameInstanceAs(Lifecycle.State.INITIALIZED)
    }
  }

  @Test
  fun createsService() {
    runPushNotificationServiceTest {
      create()
      assertThat(service)
        .prop(PushNotificationService::lifecycleState)
        .isSameInstanceAs(Lifecycle.State.CREATED)
    }
  }

  @Test
  fun setsServiceCoroutineContextToItsOwn() {
    runPushNotificationServiceTest {
      assertThat(service)
        .prop(PushNotificationService::coroutineScope)
        .prop(CoroutineScope::coroutineContext)
        .prop(CoroutineContext::job)
        .prop(Job::key)
        .isEqualTo(coroutineContext.job.key)
    }
  }

  @Test
  fun respondsToRequestForObtainingNotifications() = runPushNotificationServiceTest {
    assertThat(requester)
      .transform("get") { it.get(HostedURLBuilder::buildNotificationsRoute) }
      .isSuccessful()
      .suspendCall("bodyAsText", HttpResponse::bodyAsText)
      .transform("Json.decodeFromString") {
        Json.decodeFromString(PushNotificationService.dtosSerializer, it)
      }
  }

  @Test
  fun responseIsProvidedBySpecifiedProviderWhenRequestIsNotForObtainingNotifications() =
    runPushNotificationServiceTest {
      assertThat(requester)
        .transform("get") { it.get(HostedURLBuilder::buildNotificationSubscriptionPushingRoute) }
        .isSuccessful()
        .suspendCall("bodyAsText", HttpResponse::bodyAsText)
        .isEmpty()
    }

  @Test
  fun destroysService() {
    runPushNotificationServiceTest {
      create()
      destroy()
      assertThat(service)
        .prop(PushNotificationService::lifecycleState)
        .isSameInstanceAs(Lifecycle.State.DESTROYED)
    }
  }

  @Test
  fun serviceIsDestroyedAfterTest() {
    var service: PushNotificationService
    runPushNotificationServiceTest { service = this.service }
    assertThat(service)
      .prop(PushNotificationService::lifecycleState)
      .isSameInstanceAs(Lifecycle.State.DESTROYED)
  }
}

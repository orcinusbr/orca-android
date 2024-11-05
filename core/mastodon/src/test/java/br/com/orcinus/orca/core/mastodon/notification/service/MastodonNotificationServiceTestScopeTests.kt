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

import androidx.lifecycle.Lifecycle
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isSameInstanceAs
import assertk.assertions.prop
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.job
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class MastodonNotificationServiceTestScopeTests {
  @Test
  fun runsBodyOnce() {
    var runCount = 0
    runMastodonNotificationServiceTest { runCount++ }
    assertThat(runCount).isEqualTo(1)
  }

  @Test
  fun serviceIsInitializedByDefault() {
    runMastodonNotificationServiceTest {
      assertThat(service)
        .prop(MastodonNotificationService::lifecycleState)
        .isSameInstanceAs(Lifecycle.State.INITIALIZED)
    }
  }

  @Test
  fun createsService() {
    runMastodonNotificationServiceTest {
      create()
      assertThat(service)
        .prop(MastodonNotificationService::lifecycleState)
        .isSameInstanceAs(Lifecycle.State.CREATED)
    }
  }

  @Test
  fun setsServiceCoroutineContextToItsOwn() {
    runMastodonNotificationServiceTest {
      assertThat(service)
        .prop(MastodonNotificationService::coroutineScope)
        .prop(CoroutineScope::coroutineContext)
        .prop(CoroutineContext::job)
        .prop(Job::key)
        .isEqualTo(coroutineContext.job.key)
    }
  }

  @Test
  fun respondsToRequestForObtainingNotifications() {
    runMastodonNotificationServiceTest {
      assertThat(requester)
        .transform("get") { it.get(HostedURLBuilder::buildNotificationsRoute) }
        .transform("bodyAsText") { it.bodyAsText() }
        .transform("Json.decodeFromString") {
          Json.decodeFromString(ListSerializer(MastodonNotification.Serializer.instance), it)
        }
    }
  }

  @Test
  fun responseIsProvidedBySpecifiedProviderWhenRequestIsNotForObtainingNotifications() {
    runMastodonNotificationServiceTest {
      assertThat(requester)
        .transform("get") { it.get(HostedURLBuilder::buildNotificationSubscriptionPushingRoute) }
        .transform("body") { it.body<String>() }
        .isEmpty()
    }
  }

  @Test
  fun destroysService() {
    runMastodonNotificationServiceTest {
      create()
      destroy()
      assertThat(service)
        .prop(MastodonNotificationService::lifecycleState)
        .isSameInstanceAs(Lifecycle.State.DESTROYED)
    }
  }

  @Test
  fun serviceIsDestroyedAfterTest() {
    var service: MastodonNotificationService
    runMastodonNotificationServiceTest { service = this.service }
    assertThat(service)
      .prop(MastodonNotificationService::lifecycleState)
      .isSameInstanceAs(Lifecycle.State.DESTROYED)
  }
}

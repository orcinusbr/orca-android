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
import assertk.assertions.isFalse
import assertk.assertions.isSameInstanceAs
import assertk.assertions.isTrue
import assertk.assertions.prop
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.runAuthenticatedRequesterTest
import br.com.orcinus.orca.std.injector.Injector
import kotlin.test.Test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class PushNotificationServiceTests {
  @Test
  fun initialLifecycleStateIsInitializedOne() = runPushNotificationServiceTest {
    assertThat(service)
      .prop(PushNotificationService::lifecycleState)
      .isSameInstanceAs(Lifecycle.State.INITIALIZED)
  }

  @Test
  fun postCreationLifecycleStateIsCreatedOne() = runPushNotificationServiceTest {
    create()
    assertThat(service)
      .prop(PushNotificationService::lifecycleState)
      .isSameInstanceAs(Lifecycle.State.CREATED)
  }

  @Test
  fun postDestructionLifecycleStateIsDestroyedOne() = runPushNotificationServiceTest {
    destroy()
    assertThat(service)
      .prop(PushNotificationService::lifecycleState)
      .isSameInstanceAs(Lifecycle.State.DESTROYED)
  }

  @Test
  fun instantiatingFromEmptyConstructorRetrievesInjectedRequester() {
    var isRetrieved = false
    runAuthenticatedRequesterTest {
      Injector.injectLazily<Requester<*>> {
        isRetrieved = true
        requester
      }
      PushNotificationService()
    }
    assertThat(isRetrieved).isTrue()
    Injector.clear()
  }

  @Test
  fun coroutineScopeIsActiveByDefault() = runPushNotificationServiceTest {
    service.coroutineScope.ensureActive()
  }

  @Test
  fun setsCoroutineContext() = runPushNotificationServiceTest {
    service.setCoroutineContext(Dispatchers.IO)
    assertThat(service)
      .prop(PushNotificationService::coroutineScope)
      .prop(CoroutineScope::coroutineContext)
      .transform("[${Dispatchers.IO.key}]") { it[Dispatchers.IO.key] }
      .isSameInstanceAs(Dispatchers.IO)
  }

  @Test
  fun cancelsActiveJobsWhenSettingCoroutineContext() = runPushNotificationServiceTest {
    val job = service.coroutineScope.launch { awaitCancellation() }
    service.setCoroutineContext(Dispatchers.IO)
    assertThat(job).prop(Job::isCancelled).isTrue()
  }

  @Test
  fun cancelsCoroutineScopeWhenDestroyed() = runPushNotificationServiceTest {
    destroy()
    assertThat(service)
      .prop(PushNotificationService::coroutineScope)
      .prop(CoroutineScope::isActive)
      .isFalse()
  }
}

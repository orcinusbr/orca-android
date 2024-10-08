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

import androidx.lifecycle.Lifecycle
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isSameInstanceAs
import assertk.assertions.prop
import kotlin.test.Test
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
      assertThat(controller.get())
        .prop(MastodonNotificationService::lifecycleState)
        .isSameInstanceAs(Lifecycle.State.INITIALIZED)
    }
  }

  @Test
  fun serviceIsDestroyedAfterTest() {
    var service: MastodonNotificationService
    runMastodonNotificationServiceTest { service = controller.get() }
    assertThat(service)
      .prop(MastodonNotificationService::lifecycleState)
      .isSameInstanceAs(Lifecycle.State.DESTROYED)
  }
}
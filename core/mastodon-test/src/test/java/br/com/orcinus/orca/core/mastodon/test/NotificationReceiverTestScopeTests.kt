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

package br.com.orcinus.orca.core.mastodon.test

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isSameInstanceAs
import assertk.assertions.prop
import br.com.orcinus.orca.core.mastodon.test.notification.NotificationReceiverActivity
import br.com.orcinus.orca.core.mastodon.test.notification.runNotificationReceiverTest
import br.com.orcinus.orca.platform.starter.lifecycle.state.CompleteLifecycleState
import br.com.orcinus.orca.platform.testing.context
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class NotificationReceiverTestScopeTests {
  @Test
  fun runsBodyOnce() {
    var runCount = 0
    runNotificationReceiverTest(context) { runCount++ }
    assertThat(runCount).isEqualTo(1)
  }

  @Test
  fun runsTestInAnActivity() = runNotificationReceiverTest {
    assertThat(context)
      .isInstanceOf<NotificationReceiverActivity>()
      .prop(NotificationReceiverActivity::completeLifecycleState)
      .isSameInstanceAs(CompleteLifecycleState.RESUMED)
  }
}

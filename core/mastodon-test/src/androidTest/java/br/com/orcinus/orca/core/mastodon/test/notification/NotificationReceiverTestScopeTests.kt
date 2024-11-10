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

package br.com.orcinus.orca.core.mastodon.test.notification

import android.content.Context
import androidx.test.core.app.launchActivity
import br.com.orcinus.orca.core.mastodon.notification.NotificationReceiver
import io.mockk.spyk
import io.mockk.verify
import kotlin.test.Test

internal class NotificationReceiverTestScopeTests {
  @Test
  fun registersReceiver() {
    launchActivity<NotificationReceiverActivity>().use { scenario ->
      scenario.onActivity { activity: NotificationReceiverActivity ->
        val spiedActivity = spyk(activity)
        runNotificationReceiverTest(spiedActivity) {
          register()
          verify(exactly = 1) {
            spiedActivity.registerReceiver(
              receiver,
              NotificationReceiver.filter,
              /* broadcastPermission = */ null,
              /* scheduler = */ null,
              Context.RECEIVER_EXPORTED
            )
          }
        }
      }
    }
  }

  @Test
  fun unregistersReceiverAfterTest() {
    launchActivity<NotificationReceiverActivity>().use { scenario ->
      scenario.onActivity { activity: NotificationReceiverActivity ->
        val spiedActivity = spyk(activity)
        lateinit var receiver: NotificationReceiver
        runNotificationReceiverTest(spiedActivity) {
          receiver = this.receiver
          register()
        }
        verify(exactly = 1) { spiedActivity.unregisterReceiver(receiver) }
      }
    }
  }
}

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

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.filters.SdkSuppress
import assertk.assertFailure
import assertk.assertions.isInstanceOf
import kotlin.test.Test

internal class DefaultNotificationLockTests {
  class RequestActivity : ComponentActivity()

  @SdkSuppress(maxSdkVersion = Build.VERSION_CODES.S_V2)
  @Test
  fun registersReceiverWhenUnlockingInAnApiLevelInWhichThePermissionIsUnsupported() {
    launchActivity<RequestActivity>()
      .moveToState(Lifecycle.State.CREATED)
      ?.onActivity {
        val lock = NotificationLock(it).apply(NotificationLock::requestUnlock)

        // Context.unregisterReceiver(BroadcastReceiver) throws if the receiver is unregistered.
        assertFailure { it.unregisterReceiver(lock.receiver) }.isInstanceOf<IllegalStateException>()

        lock.close()
      }
      ?.close()
  }

  @Test
  fun unregistersReceiver() {
    launchActivity<RequestActivity>()
      .moveToState(Lifecycle.State.CREATED)
      .onActivity { activity ->
        Thread.currentThread()
        val lock =
          NotificationLock(activity).apply {
            requestUnlock()
            close()
          }

        // Context.unregisterReceiver(BroadcastReceiver) throws if the receiver is unregistered.
        assertFailure { activity.unregisterReceiver(lock.receiver) }
          .isInstanceOf<IllegalArgumentException>()
      }
      ?.close()
  }
}

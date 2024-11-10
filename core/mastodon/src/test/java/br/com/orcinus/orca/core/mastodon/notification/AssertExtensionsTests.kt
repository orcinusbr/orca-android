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

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Binder
import assertk.assertFailure
import assertk.assertions.isInstanceOf
import br.com.orcinus.orca.ext.testing.assertThat
import br.com.orcinus.orca.platform.testing.context
import kotlin.test.Test
import org.junit.runner.RunWith
import org.opentest4j.AssertionFailedError
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class AssertExtensionsTests {
  private class AssertionService : Service() {
    var binder: Binder? = null

    override fun onCreate() {
      super.onCreate()
      binder = Binder()
    }

    override fun onBind(intent: Intent?): Binder? {
      return binder
    }

    override fun onDestroy() {
      super.onDestroy()
      binder = null
    }
  }

  private class AssertionBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) = Unit
  }

  @Test
  fun failsWhenAssertingThatNonLastlyStoppedServiceIsLastlyStopped() {
    assertFailure(assertThat<AssertionService>()::isLastlyStopped)
      .isInstanceOf<AssertionFailedError>()
  }

  @Test
  fun passesWhenAssertingThatUnboundServiceIsUnbound() {
    val intent = Intent(context, AssertionService::class.java)
    context.startService(intent)
    context.stopService(intent)
    assertThat<AssertionService>().isLastlyStopped()
  }

  @Test
  fun failsWhenAssertingThatUnregisteredBroadcastReceiverIsRegistered() {
    assertFailure(assertThat<AssertionBroadcastReceiver>()::isRegistered)
      .isInstanceOf<AssertionFailedError>()
  }

  @Test
  fun passesWhenAssertingThatRegisteredBroadcastReceiverIsRegistered() {
    context.registerReceiver(
      AssertionBroadcastReceiver(),
      /** filter = */
      null,
      Context.RECEIVER_NOT_EXPORTED
    )
    assertThat<AssertionBroadcastReceiver>().isRegistered()
  }
}

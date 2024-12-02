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

package br.com.orcinus.orca.core.mastodon.notification.push

import android.content.Context
import android.content.Intent
import assertk.assertFailure
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import br.com.orcinus.orca.ext.testing.assertThat
import br.com.orcinus.orca.platform.testing.context
import io.mockk.mockk
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class PushNotificationReceiverTests {
  @Test
  fun throwsWhenInstantiatedInAContextAndABroadcastIsReceivedInAnother() {
    assertFailure {
        PushNotificationReceiver(context)
          .onReceive(mockk<Context>(), Intent(PushNotificationReceiver.ACTION))
      }
      .isInstanceOf<IllegalStateException>()
  }

  @Test
  fun throwsWhenABroadcastWithoutAnActionIsReceived() {
    assertFailure { PushNotificationReceiver(context).onReceive(context, Intent()) }
      .isInstanceOf<IllegalStateException>()
  }

  @Test
  fun throwsWhenAnUnknownBroadcastIsReceived() {
    assertFailure {
        PushNotificationReceiver(context).onReceive(context, Intent(Intent.ACTION_ALL_APPS))
      }
      .isInstanceOf<IllegalStateException>()
  }

  @Test
  fun bindsServiceWhenAKnownBroadcastIsReceived() {
    val receiver = PushNotificationReceiver(context)
    receiver.onReceive(context, Intent(PushNotificationReceiver.ACTION))
    assertThat<PushNotificationService>().bindingCount().isEqualTo(1)
    receiver.close()
  }
}

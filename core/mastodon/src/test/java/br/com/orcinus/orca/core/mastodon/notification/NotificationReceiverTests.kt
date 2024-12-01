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

import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import assertk.assertFailure
import assertk.assertions.isInstanceOf
import br.com.orcinus.orca.platform.testing.context
import kotlin.test.Test

internal class NotificationReceiverTests {
  @Test
  fun throwsWhenInstantiatedInAContextBroadcastIsReceivedInAnother() {
    assertFailure {
        NotificationReceiver(context)
          .onReceive(
            InstrumentationRegistry.getInstrumentation()?.targetContext,
            Intent(NotificationReceiver.ACTION)
          )
      }
      .isInstanceOf<IllegalStateException>()
  }

  @Test
  fun throwsWhenABroadcastWithoutAnActionIsReceived() {
    assertFailure { NotificationReceiver(context).onReceive(context, Intent()) }
      .isInstanceOf<IllegalStateException>()
  }

  @Test
  fun throwsWhenAnUnknownBroadcastIsReceived() {
    assertFailure {
        NotificationReceiver(context).onReceive(context, Intent(Intent.ACTION_ALL_APPS))
      }
      .isInstanceOf<IllegalStateException>()
  }
}

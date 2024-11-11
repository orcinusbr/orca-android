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

import android.app.NotificationChannel
import android.app.NotificationManager
import assertk.all
import assertk.assertThat
import assertk.assertions.each
import assertk.assertions.isEqualTo
import assertk.assertions.isSameInstanceAs
import assertk.assertions.prop
import br.com.orcinus.orca.platform.testing.context
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class MastodonNotificationTypeTests {
  @Test
  fun convertsIntoNotificationChannelWithTheDesignatedName() {
    assertThat(MastodonNotification.Type.entries).each { typeAssert ->
      typeAssert
        .transform("toNotificationChannel") { type -> type.toNotificationChannel(context) }
        .all {
          typeAssert.given { type ->
            prop(NotificationChannel::getName).isEqualTo(type.getChannelName(context))
          }
        }
    }
  }

  @Test
  fun convertsIntoNotificationChannelWithTheDesignatedDescription() {
    assertThat(MastodonNotification.Type.entries).each { typeAssert ->
      typeAssert
        .transform("toNotificationChannel") { type -> type.toNotificationChannel(context) }
        .all {
          typeAssert.given { type ->
            prop(NotificationChannel::getDescription).isEqualTo(type.getChannelDescription(context))
          }
        }
    }
  }

  @Test
  fun convertsIntoNotificationChannelWithTheDefaultImportance() {
    assertThat(MastodonNotification.Type.entries).each { typeAssert ->
      typeAssert
        .transform("toNotificationChannel") { type -> type.toNotificationChannel(context) }
        .all {
          prop(NotificationChannel::getImportance)
            .isSameInstanceAs(NotificationManager.IMPORTANCE_DEFAULT)
        }
    }
  }
}

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

import assertk.assertThat
import assertk.assertions.each
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import br.com.orcinus.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import java.time.ZonedDateTime
import kotlin.test.Test
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class MastodonNotificationSerializerTests {
  private val serializer = MastodonNotification.Serializer.instance
  private val notificationID = "0"
  private val notificationCreatedAt = MastodonNotification.createdAt(ZonedDateTime.now())
  private val notificationAccount = MastodonAccount.default
  private val notificationStatus = MastodonStatus.default

  @Test
  fun serializes() {
    assertThat(MastodonNotification.Type.entries).each { typeAssert ->
      typeAssert.given { type ->
        assertThat(
            Json.encodeToJsonElement(
              serializer,
              MastodonNotification(
                notificationID,
                type,
                notificationCreatedAt,
                notificationAccount,
                notificationStatus
              )
            )
          )
          .isEqualTo(
            buildJsonObject {
              put("id", notificationID)
              put("type", Json.encodeToJsonElement(type))
              put("createdAt", notificationCreatedAt)
              put("account", Json.encodeToJsonElement(notificationAccount))
              put("status", Json.encodeToJsonElement(notificationStatus))
            }
          )
      }
    }
  }

  @Test
  fun deserializes() {
    assertThat(MastodonNotification.Type.entries).each { typeAssert ->
      typeAssert.given { type ->
        assertThat(
            Json.decodeFromJsonElement<MastodonNotification>(
              serializer,
              buildJsonObject {
                put("id", notificationID)
                put("type", Json.encodeToJsonElement(type))
                put("createdAt", notificationCreatedAt)
                put("account", Json.encodeToJsonElement(notificationAccount))
                put("status", Json.encodeToJsonElement(notificationStatus))
              }
            )
          )
          .isEqualTo(
            MastodonNotification(
              notificationID,
              type,
              notificationCreatedAt,
              notificationAccount,
              notificationStatus
            )
          )
      }
    }
  }
}

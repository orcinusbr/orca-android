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
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import br.com.orcinus.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import br.com.orcinus.orca.core.mastodon.instance.requester.runRequesterTest
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import java.net.URI
import java.time.ZonedDateTime
import kotlin.test.Test
import kotlinx.serialization.json.Json

internal class MastodonNotificationsClientResponseProviderTests {
  @Test
  fun responseIsProvidedByFallbackWhenURLIsNotThatOfARequestForFetchingNotifications() {
    lateinit var baseURI: URI
    runRequesterTest(MastodonNotificationsClientResponseProvider({ baseURI })) {
      baseURI = requester.baseURI
      assertThat(requester)
        .transform("get") { it.get(route) }
        .transform("body") { it.body<String>() }
        .isEmpty()
    }
  }

  @Test
  fun setsNotification() {
    lateinit var baseURI: URI
    val notification =
      MastodonNotification(
        /* id = */ "👥📚",
        MastodonNotification.Type.POLL,
        MastodonNotification.createdAt(ZonedDateTime.now()),
        MastodonAccount.default,
        MastodonStatus.default
      )
    runRequesterTest(
      MastodonNotificationsClientResponseProvider({ baseURI }).apply {
        setNotification(notification)
      }
    ) {
      baseURI = requester.baseURI
      assertThat(requester)
        .transform("get") { it.get(HostedURLBuilder::buildNotificationsRoute) }
        .transform { it.bodyAsText() }
        .transform("Json.decodeFromString") {
          Json.decodeFromString(MastodonNotificationsClientResponseProvider.serializer, it)
        }
        .containsExactly(notification)
    }
  }
}

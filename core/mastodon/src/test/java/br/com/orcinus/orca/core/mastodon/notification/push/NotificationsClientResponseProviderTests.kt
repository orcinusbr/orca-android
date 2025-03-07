/*
 * Copyright © 2024–2025 Orcinus
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

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import assertk.coroutines.assertions.suspendCall
import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import br.com.orcinus.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import br.com.orcinus.orca.core.mastodon.instance.requester.runRequesterTest
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import br.com.orcinus.orca.std.func.test.monad.isSuccessful
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import java.net.URI
import java.time.ZonedDateTime
import kotlin.test.Test
import kotlinx.serialization.json.Json

internal class NotificationsClientResponseProviderTests {
  @Test
  fun responseIsProvidedByFallbackWhenURLIsNotThatOfARequestForFetchingNotifications() {
    lateinit var baseURI: URI
    runRequesterTest(NotificationsClientResponseProvider({ baseURI })) {
      baseURI = requester.baseURI
      assertThat(requester)
        .transform("get") { it.get(route) }
        .isSuccessful()
        .suspendCall("bodyAsText", HttpResponse::bodyAsText)
        .isEmpty()
    }
  }

  @Test
  fun setsNotification() {
    lateinit var baseURI: URI
    val notification =
      PushNotification(
        /* id = */ "👥📚",
        PushNotification.Type.POLL,
        PushNotification.createdAt(ZonedDateTime.now()),
        MastodonAccount.default,
        MastodonStatus.default
      )
    runRequesterTest(
      NotificationsClientResponseProvider({ baseURI }).apply { this.notification = notification }
    ) {
      baseURI = requester.baseURI
      assertThat(requester)
        .transform("get") { it.get(HostedURLBuilder::buildNotificationsRoute) }
        .isSuccessful()
        .suspendCall("bodyAsText", HttpResponse::bodyAsText)
        .transform("Json.decodeFromString") {
          Json.decodeFromString(PushNotificationService.dtosSerializer, it)
        }
        .containsExactly(notification)
    }
  }
}

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

import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount
import br.com.orcinus.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import br.com.orcinus.orca.core.mastodon.instance.requester.ClientResponseProvider
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.HttpRequestData
import io.ktor.http.Url
import io.ktor.http.toURI
import java.net.URI
import java.time.ZonedDateTime
import kotlinx.serialization.json.Json

/**
 * [ClientResponseProvider] that responds to a request with a [List] containing a single default
 * notification DTO in case the URL to which it was sent is that for retrieving notifications from
 * the Mastodon server. Otherwise, defaults to the given [next].
 *
 * @param getBaseURI Obtains the [URI] from which the routes are constructed.
 * @param next [ClientResponseProvider] to fall back to when the request is not for fetching
 *   notifications.
 */
internal class MastodonNotificationsClientResponseProvider(
  private val getBaseURI: () -> URI,
  private val next: ClientResponseProvider = ClientResponseProvider.ok
) : ClientResponseProvider {
  /** Single DTO to be provided in response to a request for fetching notifications. */
  private var notification =
    MastodonNotification(
      /* id = */ "0",
      MastodonNotification.Type.POLL,
      MastodonNotification.createdAt(ZonedDateTime.now()),
      MastodonAccount.default,
      MastodonStatus.default
    )

  /**
   * Whether this [Url] is that to which requests for obtaining notifications from the Mastodon
   * server are sent; ultimately, the one built by [HostedURLBuilder.buildNotificationsRoute] from
   * the given base [URI].
   *
   * @see getBaseURI
   */
  private val Url.isOfNotifications
    get() = toURI() == HostedURLBuilder.from(getBaseURI()).buildNotificationsRoute()

  override suspend fun MockRequestHandleScope.provide(requestData: HttpRequestData) =
    if (requestData.url.isOfNotifications) {
      respondOk(
        Json.encodeToString(MastodonNotificationService.dtosSerializer, listOf(notification))
      )
    } else {
      with(next) { provide(requestData) }
    }

  /**
   * Changes the DTO to be provided in response to a request to fetch the notifications.
   *
   * @param notification [MastodonNotification] to be set.
   */
  fun setNotification(notification: MastodonNotification) {
    this.notification = notification
  }
}

/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.instance

import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.Authenticator
import br.com.orcinus.orca.core.auth.Authorizer
import br.com.orcinus.orca.core.instance.InstanceProvider
import br.com.orcinus.orca.core.mastodon.instance.requester.ClientResponseProvider
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequest

/**
 * [InstanceProvider] that provides a [SampleMastodonInstance].
 *
 * @param authorizer [Authorizer] with which the user will be authorized.
 * @param authenticator [Authenticator] through which authentication can be done.
 * @param authenticationLock [AuthenticationLock] by which features can be locked or unlocked by an
 *   authentication "wall".
 * @param clientResponseProvider Defines how the [HttpClient] to an [HttpRequest].
 */
internal class TestMastodonInstanceProvider(
  private val authorizer: Authorizer,
  private val authenticator: Authenticator,
  private val authenticationLock: AuthenticationLock<Authenticator>,
  private val clientResponseProvider: ClientResponseProvider
) : InstanceProvider {
  /**
   * [SampleMastodonInstance] to be provided.
   *
   * @see provide
   */
  private val instance by lazy {
    SampleMastodonInstance(authorizer, authenticator, authenticationLock, clientResponseProvider)
  }

  override fun provide(): SampleMastodonInstance {
    return instance
  }
}

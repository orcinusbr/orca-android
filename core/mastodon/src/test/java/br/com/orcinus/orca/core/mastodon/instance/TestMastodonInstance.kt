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
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.mastodon.instance.requester.ClientResponseProvider
import br.com.orcinus.orca.core.mastodon.instance.requester.NoOpLogger
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.createHttpClientEngineFactory
import br.com.orcinus.orca.core.sample.test.instance.sample
import br.com.orcinus.orca.core.test.TestAuthenticationLock
import br.com.orcinus.orca.core.test.auth.Authenticator
import br.com.orcinus.orca.core.test.auth.AuthorizerBuilder
import br.com.orcinus.orca.ext.uri.URIBuilder
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.request.HttpRequest

/**
 * [MastodonInstance] whose [requester] responds OK to each sent [HttpRequest].
 *
 * @param authorizer [Authorizer] with which the user will be authorized.
 * @param clientResponseProvider Defines how the [requester] to an [HttpRequest].
 */
internal class TestMastodonInstance(
  authorizer: Authorizer = AuthorizerBuilder().build(),
  override val authenticator: Authenticator = Authenticator(authorizer),
  override val authenticationLock: AuthenticationLock<Authenticator> =
    TestAuthenticationLock(authenticator = authenticator),
  private val clientResponseProvider: ClientResponseProvider
) : MastodonInstance<Authorizer, Authenticator>(Instance.sample.domain, authorizer) {
  /**
   * [HttpClientEngineFactory] that creates a [MockEngine] that sends an OK response to each
   * [HttpRequest].
   */
  private val clientEngineFactory = createHttpClientEngineFactory(clientResponseProvider)

  override val feedProvider = Instance.sample.feedProvider
  override val profileProvider = Instance.sample.profileProvider
  override val profileSearcher = Instance.sample.profileSearcher
  override val postProvider = Instance.sample.postProvider
  override val requester =
    Requester(
      NoOpLogger,
      baseURI = URIBuilder.url().scheme("https").host("orca.orcinus.com.br").path("app").build(),
      clientEngineFactory
    )
}

/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.mastodon.client.test.instance

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.mastodon.client.CoreHttpClient
import com.jeanbarrossilva.orca.core.mastodon.client.Logger
import com.jeanbarrossilva.orca.core.mastodon.instance.MastodonInstance
import com.jeanbarrossilva.orca.core.sample.test.instance.sample
import com.jeanbarrossilva.orca.core.test.TestAuthenticationLock
import com.jeanbarrossilva.orca.core.test.TestAuthenticator
import com.jeanbarrossilva.orca.core.test.TestAuthorizer
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData

/**
 * [MastodonInstance] whose [client] responds OK to each sent [HttpRequest].
 *
 * @param authorizer [TestAuthorizer] with which the user will be authorized.
 * @param respond Responds to an [HttpRequest].
 */
internal class TestMastodonInstance(
  authorizer: TestAuthorizer = TestAuthorizer(),
  override val authenticator: TestAuthenticator = TestAuthenticator(authorizer),
  override val authenticationLock: AuthenticationLock<TestAuthenticator> =
    TestAuthenticationLock(authenticator = authenticator),
  private val respond: MockRequestHandleScope.(HttpRequestData) -> HttpResponseData = {
    respondOk()
  }
) : MastodonInstance<TestAuthorizer, TestAuthenticator>(Instance.sample.domain, authorizer) {
  /**
   * [HttpClientEngineFactory] that creates a [MockEngine] that sends an OK response to each
   * [HttpRequest].
   */
  private val clientEngineFactory =
    object : HttpClientEngineFactory<MockEngineConfig> {
      override fun create(block: MockEngineConfig.() -> Unit): HttpClientEngine {
        return MockEngine(respond).apply { block(config) }
      }
    }

  override val feedProvider = Instance.sample.feedProvider
  override val profileProvider = Instance.sample.profileProvider
  override val profileSearcher = Instance.sample.profileSearcher
  override val postProvider = Instance.sample.postProvider
  override val client = CoreHttpClient(clientEngineFactory, Logger.test)
}

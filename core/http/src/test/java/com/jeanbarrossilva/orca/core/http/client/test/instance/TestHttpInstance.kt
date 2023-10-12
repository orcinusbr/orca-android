package com.jeanbarrossilva.orca.core.http.client.test.instance

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.http.client.CoreHttpClient
import com.jeanbarrossilva.orca.core.http.client.Logger
import com.jeanbarrossilva.orca.core.http.instance.HttpInstance
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.instance.sample
import com.jeanbarrossilva.orca.core.test.TestAuthenticator
import com.jeanbarrossilva.orca.core.test.TestAuthorizer
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.HttpRequest

/**
 * [HttpInstance] whose [client] responds OK to each sent [HttpRequest].
 *
 * @param authorizer [TestAuthorizer] with which the user will be authorized.
 */
internal class TestHttpInstance(
  authorizer: TestAuthorizer,
  override val authenticator: TestAuthenticator,
  override val authenticationLock: AuthenticationLock<TestAuthenticator>
) : HttpInstance<TestAuthorizer, TestAuthenticator>(Instance.sample.domain, authorizer) {
  /**
   * [HttpClientEngineFactory] that creates a [MockEngine] that sends an OK response to each
   * [HttpRequest].
   */
  private val clientEngineFactory =
    object : HttpClientEngineFactory<MockEngineConfig> {
      override fun create(block: MockEngineConfig.() -> Unit): HttpClientEngine {
        return MockEngine { respondOk() }.apply { block(config) }
      }
    }

  override val feedProvider = Instance.sample.feedProvider
  override val profileProvider = Instance.sample.profileProvider
  override val profileSearcher = Instance.sample.profileSearcher
  override val tootProvider = Instance.sample.tootProvider
  override val client = CoreHttpClient(clientEngineFactory, Logger.test)
}

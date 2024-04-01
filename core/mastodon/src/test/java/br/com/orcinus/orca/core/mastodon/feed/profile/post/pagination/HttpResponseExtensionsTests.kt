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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.core.mastodon.client.Logger
import br.com.orcinus.orca.core.mastodon.client.MastodonClient
import br.com.orcinus.orca.core.mastodon.client.test.instance.test
import br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination.type.kTypeCreatorOf
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.get
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class HttpResponseExtensionsTests {
  @Test
  fun getsBody() {
    runTest {
      assertThat(
          MastodonClient(
              object : HttpClientEngineFactory<MockEngineConfig> {
                override fun create(block: MockEngineConfig.() -> Unit): MockEngine {
                  return MockEngine { respondOk("Hello, world!") }.apply { block(config) }
                }
              },
              Logger.test
            )
            .get("/")
            .body(kTypeCreatorOf<String>())
        )
        .isEqualTo("Hello, world!")
    }
  }
}

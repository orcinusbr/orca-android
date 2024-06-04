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

package br.com.orcinus.orca.core.mastodon.network.requester

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.core.mastodon.network.client.ClientResponseProvider
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.request.HttpRequestData
import io.ktor.http.Headers
import io.ktor.http.HttpMethod
import io.ktor.http.Url
import io.ktor.http.content.OutgoingContent
import io.ktor.util.Attributes
import io.ktor.util.InternalAPI
import kotlin.test.Test
import kotlin.time.Duration.Companion.days
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class CountingClientResponseProviderTests {
  private val RequesterTestScope<*>.requestData
    @OptIn(InternalAPI::class)
    get() =
      HttpRequestData(
        Url(requester.absolute(route)),
        HttpMethod.Get,
        Headers.Empty,
        object : OutgoingContent.NoContent() {},
        executionContext = Job(),
        Attributes()
      )

  @Test
  fun counts() {
    runRequesterTest {
      val provider = CountingClientResponseProvider(ClientResponseProvider.ok)
      val mockRequestHandleScope = MockRequestHandleScope(coroutineContext)
      with(provider) { repeat(1_024) { mockRequestHandleScope.provide(requestData) } }
      assertThat(provider.count).isEqualTo(1_024)
    }
  }

  @Test
  fun waits() {
    runRequesterTest {
      val scheduler = delegate.delegate.testScheduler
      val provider = CountingClientResponseProvider(ClientResponseProvider.ok)
      val mockRequestHandleScope = MockRequestHandleScope(coroutineContext)

      launch(Dispatchers.Unconfined) {
        with(provider) {
          mockRequestHandleScope.provide(requestData)
          scheduler.advanceTimeBy(8.days)
          mockRequestHandleScope.provide(requestData)
        }
      }
      provider.waitUntilCountIs(2)
      assertThat(@OptIn(ExperimentalCoroutinesApi::class) scheduler.currentTime)
        .isEqualTo(8.days.inWholeMilliseconds)
    }
  }
}

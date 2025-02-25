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

package br.com.orcinus.orca.core.mastodon.instance.requester

import assertk.all
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isSameInstanceAs
import assertk.assertions.prop
import assertk.coroutines.assertions.suspendCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class RequesterTestScopeTests {
  @Test
  fun runsBodyOnce() {
    var runCount = 0
    runRequesterTest { runCount++ }
    assertThat(runCount).isEqualTo(1)
  }

  @Test
  fun runsBodyInSpecifiedContext() {
    val context = StandardTestDispatcher()
    runRequesterTest(context = context) {
      assertThat(coroutineContext)
        .transform("[${context.key}]") { it[context.key] }
        .isSameInstanceAs(context)
    }
  }

  @Test
  fun returnsOKHttpClientEngineFactoryWhenCallingFactoryMethodWithOKClientResponseProvider() {
    val factory = httpClientEngineFactoryOf(ClientResponseProvider.ok)
    assertThat(factory).isSameInstanceAs(httpClientEngineFactoryOf(ClientResponseProvider.ok))
    runTest(@OptIn(ExperimentalCoroutinesApi::class) UnconfinedTestDispatcher()) {
      assertThat(HttpClient(factory))
        .suspendCall("get") { it.get("/api/v1/resource") }
        .all {
          prop(HttpResponse::status).isEqualTo(HttpStatusCode.OK)
          launch { suspendCall("bodyAsText", HttpResponse::bodyAsText).isEmpty() }
        }
    }
  }

  @Test
  fun createsAnHttpClientEngineFactory() = runTest {
    assertThat(
        HttpClient(httpClientEngineFactoryOf { respond("➡️") })
          .get("/api/v1/resource")
          .body<String>()
      )
      .isEqualTo("➡️")
  }
}

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

package br.com.orcinus.orca.core.mastodon.instance.requester.authentication

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isSameInstanceAs
import assertk.coroutines.assertions.suspendCall
import br.com.orcinus.orca.std.func.test.monad.isSuccessful
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlin.test.Test
import kotlinx.coroutines.test.StandardTestDispatcher

internal class AuthenticatedRequesterTestScopeTests {
  @Test
  fun runsBodyOnce() {
    var runCount = 0
    runAuthenticatedRequesterTest { runCount++ }
    assertThat(runCount).isEqualTo(1)
  }

  @Test
  fun requestsAreRespondedTo() =
    runAuthenticatedRequesterTest({ respondOk("😶") }) {
      assertThat(requester)
        .suspendCall("get") { it.get(route) }
        .isSuccessful()
        .suspendCall("bodyAsText", HttpResponse::bodyAsText)
        .isEqualTo("😶")
    }

  @Test
  fun runsBodyInSpecifiedContext() {
    val context = StandardTestDispatcher()
    runAuthenticatedRequesterTest(context = context) {
      assertThat(coroutineContext)
        .transform("[${context.key}]") { it[context.key] }
        .isSameInstanceAs(context)
    }
  }
}

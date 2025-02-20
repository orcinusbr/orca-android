/*
 * Copyright © 2025 Orcinus
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

import app.cash.turbine.test
import assertk.assertFailure
import assertk.assertions.hasMessage
import kotlin.test.Test
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

internal class FlowsTests {
  @Test
  fun awaitsItem() = runTest {
    flowOf(0).test {
      awaitItemOrThrowCause()
      awaitComplete()
    }
  }

  @Test
  fun throwsCauseWhenAwaitingForAnItemAndAnExceptionIsThrown() {
    val exception = Exception("🃏")
    runTest {
      assertFailure { flow<Nothing> { throw exception }.test { awaitItemOrThrowCause<Nothing>() } }
        .hasMessage(exception.message)
    }
  }
}

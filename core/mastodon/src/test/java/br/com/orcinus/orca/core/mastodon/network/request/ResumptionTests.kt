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

package br.com.orcinus.orca.core.mastodon.network.request

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import br.com.orcinus.orca.core.mastodon.network.request.headers.strings.serializer
import br.com.orcinus.orca.core.mastodon.network.request.memory.InMemoryRequestDaoTestRule
import io.ktor.http.Parameters
import io.ktor.util.StringValues
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Rule

internal class ResumptionTests {
  @get:Rule val inMemoryRequestDaoRule = InMemoryRequestDaoTestRule()

  @Test
  fun doesNotPersistRequestWhenNoneResumptionPrepares() {
    runTest {
      Resumption.None.prepare(
        inMemoryRequestDaoRule.dao,
        Request(
          Authentication.None,
          Request.MethodName.GET,
          "/api/v1/resource",
          Parameterization.Body.name,
          Json.encodeToString(StringValues.serializer(), Parameters.Empty)
        )
      )
      assertThat(inMemoryRequestDaoRule.dao.selectAll()).isEmpty()
    }
  }

  @Test
  fun persistsRequestWhenResumableResumptionPrepares() {
    val request =
      Request(
        Authentication.None,
        Request.MethodName.GET,
        "/api/v1/resource",
        Parameterization.Body.name,
        Json.encodeToString(StringValues.serializer(), Parameters.Empty)
      )
    runTest {
      Resumption.Resumable.prepare(inMemoryRequestDaoRule.dao, request)
      assertThat(inMemoryRequestDaoRule.dao.selectAll()).containsExactly(request)
    }
  }
}

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

package br.com.orcinus.orca.core.mastodon.network.requester.request.headers.strings

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.ktor.http.headersOf
import io.ktor.http.parametersOf
import io.ktor.util.StringValues
import kotlin.test.Test

internal class StringValuesExtensionsTests {
  @Test
  fun convertsIntoHeaders() {
    assertThat(
        StringValues.build {
            appendAll("k0", listOf("v0", "v1"))
            append("k1", "v2")
          }
          .toHeaders()
      )
      .isEqualTo(headersOf("k0" to listOf("v0", "v1"), "k1" to listOf("v2")))
  }

  @Test
  fun convertsIntoParameters() {
    assertThat(
        StringValues.build {
            appendAll("k0", listOf("v0", "v1"))
            append("k1", "v2")
          }
          .toParameters()
      )
      .isEqualTo(parametersOf("k0" to listOf("v0", "v1"), "k1" to listOf("v2")))
  }
}

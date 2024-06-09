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

package br.com.orcinus.orca.core.mastodon.instance.requester

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.statement.request
import io.ktor.http.parametersOf
import kotlin.test.Test

internal class UrlEncodedConfigurationBuilderTests {
  @Test
  fun appendsParameters() {
    runRequesterTest {
      assertThat(
          requester
            .post(route) {
              parameters {
                append("id", "0")
                append("name", "Jean")
              }
            }
            .request
            .content
            .let { it as FormDataContent }
            .formData
        )
        .isEqualTo(parametersOf("id" to listOf("0"), "name" to listOf("Jean")))
    }
  }
}

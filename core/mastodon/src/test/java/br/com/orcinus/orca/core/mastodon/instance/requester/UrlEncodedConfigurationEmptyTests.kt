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

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.prop
import assertk.coroutines.assertions.suspendCall
import br.com.orcinus.orca.std.func.test.monad.isSuccessful
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.parametersOf
import kotlin.test.Test

internal class UrlEncodedConfigurationEmptyTests {
  @Test
  fun appendsParameters() = runRequesterTest {
    assertThat(requester)
      .suspendCall("post") {
        it.post(route) {
          parameters {
            append("id", "0")
            append("name", "Jean")
          }
        }
      }
      .isSuccessful()
      .prop(HttpResponse::request)
      .prop(HttpRequest::content)
      .isInstanceOf<FormDataContent>()
      .prop(FormDataContent::formData)
      .isEqualTo(parametersOf("id" to listOf("0"), "name" to listOf("Jean")))
  }
}

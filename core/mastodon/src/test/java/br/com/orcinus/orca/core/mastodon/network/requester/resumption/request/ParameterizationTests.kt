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

package br.com.orcinus.orca.core.mastodon.network.requester.resumption.request

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.ktor.client.request.forms.formData
import io.ktor.http.Parameters
import kotlin.test.Test

internal class ParameterizationTests {
  @Test
  fun emptyStrategyContentIsEmpty() {
    assertThat(Parameterization.empty.content).isEqualTo(Parameters.Empty)
  }

  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenDeserializingIntoNonexistentStrategy() {
    Parameterization.deserialize(name = "🥸", Parameterization.empty.serializedContent)
  }

  @Test
  fun deserializesContentIntoBodyStrategy() {
    val body = Parameterization.Body(Parameters.Empty)
    assertThat(Parameterization.deserialize(body.name, body.serializedContent)).isEqualTo(body)
  }

  @Test
  fun deserializesContentIntoHeadersStrategy() {
    val headers = Parameterization.Headers(formData())
    assertThat(Parameterization.deserialize(headers.name, headers.serializedContent))
      .isEqualTo(headers)
  }
}

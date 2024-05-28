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
import assertk.assertions.isTrue
import br.com.orcinus.orca.core.mastodon.network.request.headers.strings.serializer
import io.ktor.http.Parameters
import io.ktor.util.StringValues
import kotlin.test.Test
import kotlinx.serialization.json.Json

internal class RequestTests {
  @Test(expected = IllegalStateException::class)
  fun throwsWhenCreatingRequestWithUnknownMethodName() {
    Request(
      Authentication.None,
      methodName = "🇮🇹",
      "/api/v1/resource",
      Json.encodeToString(StringValues.serializer(), StringValues.Empty)
    )
  }

  @Test
  fun invokesDeleteLambdaWhenFoldingDeleteRequest() {
    var hasDeleteLambdaBeenInvoked = false
    Request(
        Authentication.None,
        Request.MethodName.DELETE,
        "/api/v1/resource",
        Json.encodeToString(StringValues.serializer(), Parameters.Empty)
      )
      .fold(onDelete = { hasDeleteLambdaBeenInvoked = true }, onGet = {}, onPost = {})
    assertThat(hasDeleteLambdaBeenInvoked).isTrue()
  }

  @Test
  fun invokesGetLambdaWhenFoldingGetRequest() {
    var hasGetLambdaBeenInvoked = false
    Request(
        Authentication.None,
        Request.MethodName.GET,
        "/api/v1/resource",
        Json.encodeToString(StringValues.serializer(), Parameters.Empty)
      )
      .fold(onDelete = {}, onGet = { hasGetLambdaBeenInvoked = true }, onPost = {})
    assertThat(hasGetLambdaBeenInvoked).isTrue()
  }

  @Test
  fun invokesPostLambdaWhenFoldingPostRequest() {
    var hasPostLambdaBeenInvoked = false
    Request(
        Authentication.None,
        Request.MethodName.POST,
        "/api/v1/resource",
        Json.encodeToString(StringValues.serializer(), Parameters.Empty)
      )
      .fold(onDelete = {}, onGet = {}, onPost = { hasPostLambdaBeenInvoked = true })
    assertThat(hasPostLambdaBeenInvoked).isTrue()
  }
}

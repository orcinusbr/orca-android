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

package br.com.orcinus.orca.core.mastodon.network.requester.request

import assertk.assertThat
import assertk.assertions.isTrue
import io.ktor.http.Parameters
import kotlin.test.Test
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class RequestTests {
  @Test
  fun invokesGetLambdaWhenFoldingGetRequest() {
    var hasGetLambdaBeenInvoked = false
    Request(
        Authentication.None,
        Request.MethodName.GET,
        "/api/v1/resource",
        Json.encodeToString(Parameters.Empty.entries())
      )
      .fold(onGet = { hasGetLambdaBeenInvoked = true }, onPost = {})
    assertThat(hasGetLambdaBeenInvoked).isTrue()
  }

  @Test
  fun invokesPostLambdaWhenFoldingPostRequest() {
    var hasPostLambdaBeenInvoked = false
    Request(
        Authentication.None,
        Request.MethodName.POST,
        "/api/v1/resource",
        Json.encodeToString(Parameters.Empty.entries())
      )
      .fold(onGet = {}, onPost = { hasPostLambdaBeenInvoked = true })
    assertThat(hasPostLambdaBeenInvoked).isTrue()
  }
}

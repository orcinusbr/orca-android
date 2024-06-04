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
import assertk.assertions.isNotEqualTo
import assertk.assertions.isTrue
import io.ktor.http.Parameters
import kotlin.test.Test

internal class RequestTests {
  @Test
  fun generatesTheSameUniqueIDForAnEqualRequestMultipleTimes() {
    assertThat(
        Array(size = 2) {
          Request.generateID(
            Request.MethodName.GET,
            "/api/v1/resource",
            Parameterization.Body.name,
            Parameterization.Body(Parameters.Empty).serializedContent
          )
        }
      )
      .hasRepetitionCountOf(2)
  }

  @Test
  fun generatesUniqueIDsForDistinctRequests() {
    assertThat(
        Request.generateID(
          Request.MethodName.GET,
          "/api/v1/resource",
          Parameterization.Body.name,
          Parameterization.Body(Parameters.Empty).serializedContent
        )
      )
      .isNotEqualTo(
        Request.generateID(
          Request.MethodName.GET,
          "/api/v2/resource",
          Parameterization.Body.name,
          Parameterization.Body(Parameters.Empty).serializedContent
        )
      )
  }

  @Test(expected = IllegalStateException::class)
  fun throwsWhenCreatingRequestWithUnknownMethodName() {
    Request(
      methodName = "🇮🇹",
      "/api/v1/resource",
      Parameterization.Body.name,
      Parameterization.Body(Parameters.Empty).serializedContent,
      timestamp = 0
    )
  }

  @Test
  fun invokesDeleteLambdaWhenFoldingDeleteRequest() {
    var hasDeleteLambdaBeenInvoked = false
    Request(
        Request.MethodName.DELETE,
        "/api/v1/resource",
        Parameterization.Body.name,
        Parameterization.Body(Parameters.Empty).serializedContent,
        timestamp = 0
      )
      .fold(onDelete = { hasDeleteLambdaBeenInvoked = true }, onGet = {}, onPost = {})
    assertThat(hasDeleteLambdaBeenInvoked).isTrue()
  }

  @Test
  fun invokesGetLambdaWhenFoldingGetRequest() {
    var hasGetLambdaBeenInvoked = false
    Request(
        Request.MethodName.GET,
        "/api/v1/resource",
        Parameterization.Body.name,
        Parameterization.Body(Parameters.Empty).serializedContent,
        timestamp = 0
      )
      .fold(onDelete = {}, onGet = { hasGetLambdaBeenInvoked = true }, onPost = {})
    assertThat(hasGetLambdaBeenInvoked).isTrue()
  }

  @Test
  fun invokesPostLambdaWhenFoldingPostRequest() {
    var hasPostLambdaBeenInvoked = false
    Request(
        Request.MethodName.POST,
        "/api/v1/resource",
        Parameterization.Body.name,
        Parameterization.Body(Parameters.Empty).serializedContent,
        timestamp = 0
      )
      .fold(onDelete = {}, onGet = {}, onPost = { hasPostLambdaBeenInvoked = true })
    assertThat(hasPostLambdaBeenInvoked).isTrue()
  }
}

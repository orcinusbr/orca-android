/*
 * Copyright © 2023–2025 Orcinus
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

import assertk.Assert
import assertk.assertThat
import assertk.assertions.prop
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.mastodon.instance.requester.InternalRequesterApi
import br.com.orcinus.orca.std.func.monad.Maybe
import br.com.orcinus.orca.std.func.test.monad.isSuccessful
import io.ktor.client.request.HttpRequest
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

/**
 * Creates an [Assert] on the [responseMaybe]'s authorization [Headers].
 *
 * @param responseMaybe [Result] with a [HttpResponse] whose header will be the basis of the
 *   [Assert].
 */
@InternalRequesterApi
internal fun assertThatRequestAuthorizationHeaderOf(
  responseMaybe: Maybe<AuthenticationLock.FailedAuthenticationException, HttpResponse>
) =
  assertThat(responseMaybe)
    .isSuccessful()
    .prop(HttpResponse::request)
    .prop(HttpRequest::headers)
    .transform("get") { it[HttpHeaders.Authorization] }

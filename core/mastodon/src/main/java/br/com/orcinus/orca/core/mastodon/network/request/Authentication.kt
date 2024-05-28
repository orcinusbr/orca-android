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

import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.SomeAuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth

/**
 * Denotes whether the [Actor] by which a request is being performed is required to be
 * authenticated.
 */
enum class Authentication {
  /** Denotes that the [Actor] doesn't need to be authenticated for a given request. */
  None {
    override suspend fun lock(lock: SomeAuthenticationLock, requestBuilder: HttpRequestBuilder) {}
  },

  /**
   * Denotes that authentication should be scheduled when the current [Actor] isn't authenticated in
   * order to perform a certain request.
   */
  Scheduled {
    override suspend fun lock(lock: SomeAuthenticationLock, requestBuilder: HttpRequestBuilder) {
      lock.scheduleUnlock { requestBuilder.bearerAuth(it.accessToken) }
    }
  };

  /**
   * Either performs authentication or doesn't based on what this requirement specifies.
   *
   * @param lock [AuthenticationLock] by which authentication may be requested to be performed.
   * @param requestBuilder [HttpRequestBuilder] responsible for building the HTTP request.
   */
  internal abstract suspend fun lock(
    lock: SomeAuthenticationLock,
    requestBuilder: HttpRequestBuilder
  )
}

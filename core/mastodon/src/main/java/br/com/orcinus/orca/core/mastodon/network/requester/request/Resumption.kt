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

/** Indicates whether a given request should be resumed whenever it is abruptly interrupted. */
enum class Resumption {
  /**
   * Indicates that the request shouldn't be performed again after an interruption followed by a
   * request for resumption.
   */
  None {
    override suspend fun prepare(dao: RequestDao, request: Request) {}
  },

  /**
   * Indicates that the request should be resumed after it's been interrupted and it is requested
   * for it to be performed again.
   */
  Resumable {
    override suspend fun prepare(dao: RequestDao, request: Request) {
      dao.insert(request)
    }
  };

  /**
   * Prepares a request for resumption depending on the specific behavior of this policy.
   *
   * @param dao [RequestDao] by which information about the request can be persisted.
   * @param request Data regarding the request to be resumed.
   */
  internal abstract suspend fun prepare(dao: RequestDao, request: Request)
}

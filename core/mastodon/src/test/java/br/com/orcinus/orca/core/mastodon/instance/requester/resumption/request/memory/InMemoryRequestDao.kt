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

package br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.memory

import br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.Request
import br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.RequestDao

/** [RequestDao] that inserts [Request]s into and deletes them from memory. */
internal class InMemoryRequestDao : RequestDao() {
  /** Inserted [Request]s to be retrieved or deleted. */
  private val requests = mutableListOf<Request>()

  override suspend fun selectAll(): List<Request> {
    return requests.toList()
  }

  override suspend fun selectByID(id: Int): Request? {
    return requests.find { it.id == id }
  }

  override suspend fun insert(request: Request) {
    requests.add(request)
  }

  override suspend fun delete(request: Request) {
    requests.remove(request)
  }

  override suspend fun clear() {
    requests.clear()
  }
}

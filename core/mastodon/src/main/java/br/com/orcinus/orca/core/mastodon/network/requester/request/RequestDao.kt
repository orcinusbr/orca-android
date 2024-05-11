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

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.orcinus.orca.core.mastodon.network.requester.InternalRequesterApi
import io.ktor.http.Parameters
import kotlinx.serialization.KSerializer

/** DAO by which [Request]-related read and write operations are performed. */
@Dao
@InternalRequesterApi
internal abstract class RequestDao {
  /**
   * Selects all of the [Request]s that have been previously inserted.
   *
   * @see insert
   */
  @Query("SELECT * FROM requests") abstract suspend fun selectAll(): List<Request>

  /**
   * Inserts the [request].
   *
   * @param request [Request] to be inserted.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE) abstract suspend fun insert(request: Request)

  /**
   * Deletes the inserted [Request].
   *
   * @param request [Request] to be deleted.
   * @see insert
   */
  @Delete abstract suspend fun delete(request: Request)

  /**
   * Deletes all of the [Request]s that have been inserted.
   *
   * @see insert
   */
  @Query("DELETE FROM requests") abstract suspend fun clear()

  /**
   * Inserts a [Request].
   *
   * @param authentication Authentication requirement that's been deemed appropriate.
   * @param methodName Name of the HTTP method that's been called on the [route].
   * @param route Specific resource to which access was performed of the [Request] to be inserted.
   * @param parameters [Parameters] represented as a JSON object converted by a [KSerializer] into a
   *   [String].
   * @return The [Request] that's been inserted.
   * @see Parameters.Companion.serializer
   */
  suspend fun insert(
    authentication: Authentication,
    @Request.MethodName methodName: String,
    route: String,
    parameters: String
  ): Request {
    val request = Request(authentication, methodName, route, parameters)
    insert(request)
    return request
  }
}

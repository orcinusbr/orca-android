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

package br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.orcinus.orca.core.mastodon.instance.requester.InternalRequesterApi

/** DAO by which [Request]-related read and write operations are performed. */
@Dao
internal abstract class RequestDao {
  /**
   * Selects all of the [Request]s that have been previously inserted.
   *
   * @see insert
   */
  @InternalRequesterApi
  @Query("SELECT * FROM requests")
  abstract suspend fun selectAll(): List<Request>

  /**
   * Selects a [Request] by its ID.
   *
   * @param id ID of the [Request] to be selected.
   * @see Request.id
   */
  @InternalRequesterApi
  @Query("SELECT * FROM requests WHERE id = :id")
  abstract suspend fun selectByID(id: Int): Request?

  /**
   * Inserts the [request].
   *
   * @param request [Request] to be inserted.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  @InternalRequesterApi
  abstract suspend fun insert(request: Request)

  /**
   * Deletes the inserted [Request].
   *
   * @param request [Request] to be deleted.
   * @see insert
   */
  @Delete @InternalRequesterApi abstract suspend fun delete(request: Request)

  /**
   * Deletes all of the [Request]s that have been inserted.
   *
   * @see insert
   */
  @Query("DELETE FROM requests") @InternalRequesterApi abstract suspend fun clear()
}

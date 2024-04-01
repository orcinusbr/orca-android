/*
 * Copyright © 2023–2024 Orcinus
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

package com.jeanbarrossilva.orca.platform.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/** Data access object for reading and writing [Access]es. */
@Dao
internal interface AccessDao {
  /**
   * Selects the [Access] that matches the given [key] and [type].
   *
   * @param key Unique identifier to which the stored value is associated.
   * @param type [Access.Type] of the [Access] to be selected.
   */
  @Query("SELECT * FROM accesses WHERE `key` = :key AND type = :type")
  suspend fun select(key: String, type: Access.Type): Access

  /**
   * Inserts the [access].
   *
   * @param access [Access] to be inserted.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(access: Access)
}

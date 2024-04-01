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

import androidx.annotation.Discouraged
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Holds the [key] to which a value has been associated and the moment in which it was last
 * accessed.
 *
 * @param key Unique identifier of the stored value.
 * @param type [Type] that indicates how the value was accessed.
 * @param time Time that's been elapsed when the access occurred.
 */
@Entity(tableName = "accesses")
internal data class Access(val key: String, val type: Type, val time: Long) {
  /** Unique identifier composed by the [key] and the [type]. */
  @PrimaryKey
  var id = "$type:$key"
    @Discouraged(message = "ID should not be changed manually.") set

  /** Indicates how a value was accessed. */
  enum class Type {
    /** Indicates that a value was written and read. */
    IDLE,

    /** Indicates that a value was written (created or updated). */
    ALIVE
  }
}

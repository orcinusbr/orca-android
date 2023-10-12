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

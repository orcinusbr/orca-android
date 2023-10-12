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

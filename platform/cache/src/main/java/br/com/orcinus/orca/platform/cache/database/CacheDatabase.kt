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

package br.com.orcinus.orca.platform.cache.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.orcinus.orca.platform.cache.Cache

/** [RoomDatabase] from which [Cache]-related entities are read and written to. */
@Database(entities = [Access::class], version = 1)
internal abstract class CacheDatabase : RoomDatabase() {
  /** [AccessDao] that performs [Access]-related operations. */
  abstract val accessDao: AccessDao

  /** Provides a [CacheDatabase] through [provide]. */
  fun interface Provider {
    /** Provides a [CacheDatabase]. */
    fun provide(): CacheDatabase
  }

  companion object {
    /** [CacheDatabase]s associated to their respective file name. */
    private val instantiation = HashMap<String, CacheDatabase>()

    /**
     * Creates or gets a preexistent instance of a [CacheDatabase].
     *
     * @param context [Context] with which the [CacheDatabase] will be obtained.
     * @param name Name of the [CacheDatabase].
     */
    fun of(context: Context, name: String): CacheDatabase {
      return instantiation.getOrPut(name) {
        Room.databaseBuilder(context, CacheDatabase::class.java, name).build()
      }
    }
  }
}

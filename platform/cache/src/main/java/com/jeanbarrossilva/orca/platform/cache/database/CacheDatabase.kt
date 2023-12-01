/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.platform.cache.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jeanbarrossilva.orca.platform.cache.Cache

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

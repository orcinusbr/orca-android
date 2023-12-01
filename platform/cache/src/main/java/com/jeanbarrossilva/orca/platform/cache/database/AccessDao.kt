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

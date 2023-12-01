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

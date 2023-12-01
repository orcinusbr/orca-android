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

package com.jeanbarrossilva.orca.platform.cache

/** Stores values that have been fetched for later retrieval. */
abstract class Storage<T> {
  /**
   * Stores the [value], associating it to the given [key] for it to be posteriorly retrieved.
   *
   * @param key Unique identifier of the [value] to be stored.
   * @param value Value that is associated to the [key] and has been requested to be stored.
   */
  internal suspend fun store(key: String, value: T) {
    onStore(key, value)
  }

  /**
   * Returns whether a value associated to the [key] has been stored.
   *
   * @param key Unique identifier of the value whose presence will be checked.
   */
  internal suspend fun contains(key: String): Boolean {
    return onContains(key)
  }

  /**
   * Gets the value that has been stored and is associated to the given [key].
   *
   * @param key Unique identifier of the value to be obtained.
   */
  internal suspend fun get(key: String): T {
    return onGet(key)
  }

  /**
   * Removes the value that has been stored and is associated to the given [key].
   *
   * @param key Unique identifier of the value to be removed.
   */
  internal suspend fun remove(key: String) {
    onRemove(key)
  }

  /** Removes all stored values. */
  internal suspend fun clear() {
    onClear()
  }

  /**
   * Operation to be performed whenever the [value] is requested to be stored while associated to
   * the given [key].
   *
   * @param key Unique identifier of the value that has been requested to be stored.
   * @param value Value that is associated to the [key] and has been requested to be stored.
   */
  protected abstract suspend fun onStore(key: String, value: T)

  /**
   * Returns whether a value associated to the [key] has been stored.
   *
   * @param key Unique identifier of the value whose presence will be checked.
   */
  protected abstract suspend fun onContains(key: String): Boolean

  /**
   * Gets the value that has been stored and is associated to the given [key].
   *
   * @param key Unique identifier of the value to be obtained.
   */
  protected abstract suspend fun onGet(key: String): T

  /**
   * Removes the value that has been stored and is associated to the given [key].
   *
   * @param key Unique identifier of the value to be removed.
   */
  protected abstract suspend fun onRemove(key: String)

  /** Removes all stored values. */
  protected abstract suspend fun onClear()
}

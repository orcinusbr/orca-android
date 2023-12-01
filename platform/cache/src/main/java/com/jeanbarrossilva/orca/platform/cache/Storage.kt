/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
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

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

package com.jeanbarrossilva.orca.platform.cache

import android.content.Context
import com.jeanbarrossilva.orca.platform.cache.database.Access
import com.jeanbarrossilva.orca.platform.cache.database.AccessDao
import com.jeanbarrossilva.orca.platform.cache.database.CacheDatabase
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Decides whether values should be fetched or have their cached version retrieved.
 *
 * @param context [Context] through which an instance of the underlying [CacheDatabase] will be
 *   obtained.
 * @param name Identifier for this [Cache].
 */
abstract class Cache<T> internal constructor(context: Context, name: String) {
  /** [CacheDatabase] provided by the [databaseProvider]. */
  private val database
    get() = databaseProvider.provide()

  /** [AccessDao] from which living and idling [Access]es are read and written to. */
  private val accessDao
    get() = database.accessDao

  /** Elapsed time provided by the [elapsedTimeProvider]. */
  private val elapsedTime
    get() = elapsedTimeProvider.provide()

  /** [CacheDatabase.Provider] by which a [CacheDatabase] will be provided. */
  internal open val databaseProvider = CacheDatabase.Provider { CacheDatabase.of(context, name) }

  /** [ElapsedTimeProvider] for accessing the current elapsed time. */
  internal open val elapsedTimeProvider = ElapsedTimeProvider.system

  /** [Fetcher] through which values will be obtained from their source (normally the network). */
  protected abstract val fetcher: Fetcher<T>

  /** [Storage] for fetched values to be stored in and retrieved from. */
  protected abstract val storage: Storage<T>

  /**
   * Time-to-idle is the expiration threshold related to the last time a value has been read or
   * written.
   */
  protected open val timeToIdle: Duration = 1.minutes

  /** Time-to-live is similar to [timeToIdle], but only considers write operations. */
  protected open val timeToLive: Duration = 30.seconds

  /** Provides the amount of time that has passed through [provide]. */
  internal fun interface ElapsedTimeProvider {
    /** Provides the amount of time that has passed. */
    fun provide(): Duration

    companion object {
      /** [ElapsedTimeProvider] that provides the system's current time in [milliseconds]. */
      val system = ElapsedTimeProvider { System.currentTimeMillis().milliseconds }
    }
  }

  /**
   * Gets the value bound to the given [key] either by retrieving it from the [storage] if it's been
   * cached or fetches it through the [fetcher] if it hasn't, respecting both the [timeToIdle] and
   * the [timeToLive].
   *
   * @param key Unique identifier to which the value to be obtained is associated to.
   */
  suspend fun get(key: String): T {
    val isActive = isIdle(key) || isAlive(key)
    return if (isActive) retrieve(key) else remember(key)
  }

  /** Removes all [Access]es and stored values and closes the [database]. */
  internal suspend fun terminate() {
    storage.clear()
    database.clearAllTables()
    database.close()
  }

  /**
   * Returns whether the value associated to the given [key] is idle.
   *
   * @param key Unique identifier of the value.
   * @see timeToIdle
   */
  private suspend fun isIdle(key: String): Boolean {
    return storage.contains(key) && getTimeSinceLastAccessTo(key, Access.Type.IDLE) < timeToIdle
  }

  /**
   * Returns whether the value associated to the given [key] is alive.
   *
   * @param key Unique identifier of the value.
   * @see timeToLive
   */
  private suspend fun isAlive(key: String): Boolean {
    return storage.contains(key) && getTimeSinceLastAccessTo(key, Access.Type.ALIVE) < timeToLive
  }

  /**
   * Gets the amount of time that has passed since the value associated to the [key] was accessed.
   *
   * @param key Unique identifier of the value that has been accessed.
   * @param type [Access.Type] that indicates how the value was accessed.
   */
  private suspend fun getTimeSinceLastAccessTo(key: String, type: Access.Type): Duration {
    return elapsedTime - accessDao.select(key, type).time.milliseconds
  }

  /**
   * Marks the value to which the [key] is associated as idle and gets its previously stored value.
   *
   * @param key Unique identifier of the value to be retrieved.
   */
  private suspend fun retrieve(key: String): T {
    markAsIdle(key)
    return storage.get(key)
  }

  /**
   * Fetches the value associated to the given [key] and stores it, marking it as both idle and
   * alive.
   *
   * @param key Unique identifier of the value to be fetched and stored.
   * @return Value that's been fetched/stored.
   * @see markAsIdle
   * @see markAsAlive
   */
  private suspend fun remember(key: String): T {
    val value = fetcher.fetch(key)
    storage.store(key, value)
    markAsAlive(key)
    markAsIdle(key)
    return value
  }

  /**
   * Adds an alive access keyed as [key], bound to the current [elapsedTime].
   *
   * @param key Unique identifier of the value to be marked as idle.
   * @see timeToLive
   */
  private suspend fun markAsAlive(key: String) {
    val access = Access(key, Access.Type.ALIVE, elapsedTime.inWholeMilliseconds)
    accessDao.insert(access)
  }

  /**
   * Adds an idle access keyed as [key], bound to the current [elapsedTime].
   *
   * @param key Unique identifier of the value to be marked as idle.
   * @see timeToIdle
   */
  private suspend fun markAsIdle(key: String) {
    val access = Access(key, Access.Type.IDLE, elapsedTime.inWholeMilliseconds)
    accessDao.insert(access)
  }

  companion object {
    /**
     * Creates a [Cache].
     *
     * @param context [Context] through which an instance of the underlying [CacheDatabase] will be
     *   obtained.
     * @param name Identifier of the [Cache] to be created.
     * @param fetcher [Fetcher] through which values will be obtained from their source (normally
     *   the network).
     * @param storage [Storage] for fetched values to be stored in and retrieved from.
     */
    fun <T> of(context: Context, name: String, fetcher: Fetcher<T>, storage: Storage<T>): Cache<T> {
      return object : Cache<T>(context, name) {
        override val fetcher = fetcher
        override val storage = storage
      }
    }
  }
}

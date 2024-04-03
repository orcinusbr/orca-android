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

package br.com.orcinus.orca.platform.cache.memory

import androidx.test.platform.app.InstrumentationRegistry
import br.com.orcinus.orca.platform.cache.Cache
import br.com.orcinus.orca.platform.cache.database.CacheDatabase
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler

internal class InMemoryCache(
  private val coroutineScheduler: TestCoroutineScheduler,
  override val databaseProvider: CacheDatabase.Provider
) : Cache<Char>(InstrumentationRegistry.getInstrumentation().context, NAME) {
  override var fetcher = InMemoryFetcher()
    private set

  override var storage = InMemoryStorage()
    private set

  override var timeToIdle = 1.days
    private set

  override var timeToLive = 30.minutes
    private set

  override val elapsedTimeProvider = ElapsedTimeProvider {
    @OptIn(ExperimentalCoroutinesApi::class) coroutineScheduler.currentTime.milliseconds
  }

  fun fetchingWith(fetcher: InMemoryFetcher): InMemoryCache {
    return apply { this.fetcher = fetcher }
  }

  fun storingTo(storage: InMemoryStorage): InMemoryCache {
    return apply { this.storage = storage }
  }

  fun idlingFor(timeToIdle: Duration): InMemoryCache {
    return apply { this.timeToIdle = timeToIdle }
  }

  fun livingFor(timeToLive: Duration): InMemoryCache {
    return apply { this.timeToLive = timeToLive }
  }

  companion object {
    const val NAME = "test-cache"
  }
}

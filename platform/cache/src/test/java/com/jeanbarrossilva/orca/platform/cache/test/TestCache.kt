package com.jeanbarrossilva.orca.platform.cache.test

import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.orca.platform.cache.Cache
import com.jeanbarrossilva.orca.platform.cache.database.CacheDatabase
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler

internal class TestCache(
    private val coroutineScheduler: TestCoroutineScheduler,
    override val databaseProvider: CacheDatabase.Provider
) : Cache<Char>(InstrumentationRegistry.getInstrumentation().context, NAME) {
    override var fetcher = TestFetcher()
        private set

    override var storage = TestStorage()
        private set

    override var timeToIdle = 1.days
        private set

    override var timeToLive = 30.minutes
        private set

    override val elapsedTimeProvider = ElapsedTimeProvider {
        @OptIn(ExperimentalCoroutinesApi::class)
        coroutineScheduler.currentTime.milliseconds
    }

    fun fetchingWith(fetcher: TestFetcher): TestCache {
        return apply {
            this.fetcher = fetcher
        }
    }

    fun storingTo(storage: TestStorage): TestCache {
        return apply {
            this.storage = storage
        }
    }

    fun idlingFor(timeToIdle: Duration): TestCache {
        return apply {
            this.timeToIdle = timeToIdle
        }
    }

    fun livingFor(timeToLive: Duration): TestCache {
        return apply {
            this.timeToLive = timeToLive
        }
    }

    companion object {
        const val NAME = "test-cache"
    }
}

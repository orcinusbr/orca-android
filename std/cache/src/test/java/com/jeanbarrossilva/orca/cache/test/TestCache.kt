package com.jeanbarrossilva.orca.cache.test

import com.jeanbarrossilva.orca.cache.Cache
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler

internal class TestCache(
    coroutineScheduler: TestCoroutineScheduler,
    override val storage: TestStorage = TestStorage(),
    override val fetcher: TestFetcher = TestFetcher(),
    override val timeToIdle: Duration = 24.hours,
    override val timeToLive: Duration = 30.minutes
) : Cache<Int, Char>() {
    override val elapsedTimeProvider = ElapsedTimeProvider {
        @OptIn(ExperimentalCoroutinesApi::class)
        coroutineScheduler.currentTime.milliseconds
    }
}

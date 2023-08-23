package com.jeanbarrossilva.orca.cache

import com.jeanbarrossilva.orca.cache.test.TestCache
import com.jeanbarrossilva.orca.cache.test.TestFetcher
import com.jeanbarrossilva.orca.cache.test.TestStorage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

internal class CacheTests {
    @Test
    fun `GIVEN a value that's not remembered WHEN getting it THEN it's fetched`() {
        val fetcher = spy(TestFetcher())
        runTest {
            TestCache(testScheduler, fetcher = fetcher).get(0)
            verify(fetcher).fetch(0)
        }
    }

    @Test
    fun `GIVEN a value that's not remembered WHEN getting it THEN it is`() {
        val storage = TestStorage()
        runTest {
            val value = TestCache(testScheduler, storage = storage).get(0)
            assertEquals(value, storage.get(0))
        }
    }

    @Test
    fun `GIVEN a remembered value WHEN reading it before time to idle has expired THEN the remembered one is returned`() { // ktlint-disable max-line-length
        val fetcher = spy(TestFetcher())
        val storage = spy(TestStorage())
        runTest {
            val cache =
                TestCache(testScheduler, storage, fetcher, timeToIdle = 1.days, timeToLive = 1.days)
            cache.get(0)

            @OptIn(ExperimentalCoroutinesApi::class)
            advanceTimeBy(23.hours)

            cache.get(0)
            verify(fetcher).fetch(0)
            verify(storage, times(1)).get(0)
        }
    }

    @Test
    fun `GIVEN a remembered value WHEN reading it after time to idle has expired THEN it's remembered again`() { // ktlint-disable max-line-length
        val fetcher = spy(TestFetcher())
        val storage = spy(TestStorage())
        runTest {
            val cache = TestCache(testScheduler, storage, fetcher, timeToIdle = 1.days)
            cache.get(0)

            @OptIn(ExperimentalCoroutinesApi::class)
            advanceTimeBy(25.hours)

            cache.get(0)
            verify(fetcher, times(2)).fetch(0)
            verify(storage, times(2)).store(0, TestFetcher.FETCHED.first())
        }
    }

    @Test
    fun `GIVEN a remembered value WHEN reading it before time to live has expired THEN the remembered one is returned`() { // ktlint-disable max-line-length
        val fetcher = spy(TestFetcher())
        val storage = spy(TestStorage())
        runTest {
            val cache = TestCache(testScheduler, storage, fetcher, timeToLive = 1.days)
            cache.get(0)

            @OptIn(ExperimentalCoroutinesApi::class)
            advanceTimeBy(23.hours)

            cache.get(0)
            verify(fetcher).fetch(0)
            verify(storage).get(0)
        }
    }

    @Test
    fun `GIVEN a remembered value WHEN reading it after time to live has expired THEN the it's remembered again`() { // ktlint-disable max-line-length
        val fetcher = spy(TestFetcher())
        val storage = spy(TestStorage())
        runTest {
            val cache = TestCache(testScheduler, storage, fetcher, timeToLive = 1.days)
            cache.get(0)

            @OptIn(ExperimentalCoroutinesApi::class)
            advanceTimeBy(25.hours)

            cache.get(0)
            verify(fetcher, times(2)).fetch(0)
            verify(storage, times(2)).store(0, TestFetcher.FETCHED.first())
        }
    }
}

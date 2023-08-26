package com.jeanbarrossilva.orca.platform.cache

import com.jeanbarrossilva.orca.platform.cache.test.CacheTestRule
import com.jeanbarrossilva.orca.platform.cache.test.TestFetcher
import com.jeanbarrossilva.orca.platform.cache.test.TestStorage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class CacheTests {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val coroutineScope = TestScope(UnconfinedTestDispatcher())

    @get:Rule
    val cacheRule = CacheTestRule(coroutineScope)

    @Test
    fun `GIVEN a value that's not remembered WHEN getting it THEN it's fetched`() {
        val fetcher = spy(TestFetcher())
        coroutineScope.runTest {
            cacheRule.cache.fetchingWith(fetcher).get("0")
            verify(fetcher).fetch("0")
        }
    }

    @Test
    fun `GIVEN a value that's not remembered WHEN getting it THEN it is`() {
        val storage = TestStorage()
        coroutineScope.runTest {
            val value = cacheRule.cache.storingTo(storage).get("0")
            assertEquals(value, storage.get("0"))
        }
    }

    @Test
    fun `GIVEN a remembered value WHEN reading it before time to idle has expired THEN the remembered one is returned`() { // ktlint-disable max-line-length
        val fetcher = spy(TestFetcher())
        val storage = spy(TestStorage())
        val cache =
            cacheRule.cache.storingTo(storage).fetchingWith(fetcher).idlingFor(1.days).livingFor(
                1.days
            )
        coroutineScope.runTest {
            cache.get("0")

            @OptIn(ExperimentalCoroutinesApi::class)
            advanceTimeBy(23.hours)

            cache.get("0")
            verify(fetcher).fetch("0")
            verify(storage, times(1)).get("0")
        }
    }

    @Test
    fun `GIVEN a remembered value WHEN reading it after time to idle has expired THEN it's remembered again`() { // ktlint-disable max-line-length
        val fetcher = spy(TestFetcher())
        val storage = spy(TestStorage())
        val cache = cacheRule.cache.storingTo(storage).fetchingWith(fetcher).idlingFor(1.days)
        coroutineScope.runTest {
            cache.get("0")

            @OptIn(ExperimentalCoroutinesApi::class)
            advanceTimeBy(25.hours)

            cache.get("0")
            verify(fetcher, times(2)).fetch("0")
            verify(storage, times(2)).store("0", TestFetcher.FETCHED.first())
        }
    }

    @Test
    fun `GIVEN a remembered value WHEN reading it before time to live has expired THEN the remembered one is returned`() { // ktlint-disable max-line-length
        val fetcher = spy(TestFetcher())
        val storage = spy(TestStorage())
        val cache = cacheRule.cache.storingTo(storage).fetchingWith(fetcher).livingFor(1.days)
        coroutineScope.runTest {
            cache.get("0")

            @OptIn(ExperimentalCoroutinesApi::class)
            advanceTimeBy(23.hours)

            cache.get("0")
            verify(fetcher).fetch("0")
            verify(storage).get("0")
        }
    }

    @Test
    fun `GIVEN a remembered value WHEN reading it after time to live has expired THEN the it's remembered again`() { // ktlint-disable max-line-length
        val fetcher = spy(TestFetcher())
        val storage = spy(TestStorage())
        val cache = cacheRule.cache.storingTo(storage).fetchingWith(fetcher).livingFor(1.days)
        coroutineScope.runTest {
            cache.get("0")

            @OptIn(ExperimentalCoroutinesApi::class)
            advanceTimeBy(25.hours)

            cache.get("0")
            verify(fetcher, times(2)).fetch("0")
            verify(storage, times(2)).store("0", TestFetcher.FETCHED.first())
        }
    }
}

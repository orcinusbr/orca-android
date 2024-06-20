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

package br.com.orcinus.orca.platform.cache

import br.com.orcinus.orca.platform.cache.memory.InMemoryFetcher
import br.com.orcinus.orca.platform.cache.memory.InMemoryStorage
import io.mockk.coVerify
import io.mockk.spyk
import kotlin.coroutines.Continuation
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
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class CacheTests {
  /*
   * Verifications on calls to spies' suspend methods are performed reflectively due to a
   * 3+-year-old unresolved bug of MockK (as of 1.13.11) that occurs when running these tests on a
   * release build.
   *
   * Given that their JVM signatures contain both a mangled name and a trailing coroutine
   * continuation parameter, these methods need to be JvmName-annotated for the actual name to be
   * enforced and the passing of the continuation parameter to be considered in the verification
   * (with the latter being the bottleneck when verifying in a non-reflective way).
   *
   * Issue on GitHub
   * https://github.com/mockk/mockk/issues/339
   *
   * Reflection workaround, proposed by palatin
   * https://github.com/mockk/mockk/issues/339#issuecomment-661818071
   */

  @OptIn(ExperimentalCoroutinesApi::class)
  private val coroutineScope = TestScope(UnconfinedTestDispatcher())

  @get:Rule val cacheRule = CacheTestRule(coroutineScope)

  @Test
  fun fetchesWhenValueIsObtainedForTheFirstTime() {
    val fetcher = spyk(InMemoryFetcher())
    coroutineScope.runTest {
      cacheRule.cache.fetchingWith(fetcher).get("0")
      coVerify(exactly = 1) { InMemoryFetcher::fetch.call(fetcher, "0", any<Continuation<Unit>>()) }
    }
  }

  @Test
  fun remembersValueWhenItIsObtainedForTheFirstTime() {
    val storage = InMemoryStorage()
    coroutineScope.runTest {
      val value = cacheRule.cache.storingInto(storage).get("0")
      assertEquals(value, storage.get("0"))
    }
  }

  @Test
  fun obtainsRememberedValueWhenItIsReadBeforeTimeToIdle() {
    val fetcher = spyk(InMemoryFetcher())
    val storage = spyk(InMemoryStorage())
    val cache =
      cacheRule.cache.storingInto(storage).fetchingWith(fetcher).idlingFor(1.days).livingFor(1.days)
    coroutineScope.runTest {
      cache.get("0")
      @OptIn(ExperimentalCoroutinesApi::class) advanceTimeBy(23.hours)
      cache.get("0")
      coVerify(exactly = 1) { InMemoryFetcher::fetch.call(fetcher, "0", any<Continuation<Unit>>()) }
      coVerify(exactly = 1) { InMemoryStorage::get.call(storage, "0", any<Continuation<Unit>>()) }
    }
  }

  @Test
  fun remembersValueAgainWhenItIsObtainedAfterTimeToIdle() {
    val fetcher = spyk(InMemoryFetcher())
    val storage = spyk(InMemoryStorage())
    val cache = cacheRule.cache.storingInto(storage).fetchingWith(fetcher).idlingFor(1.days)
    coroutineScope.runTest {
      cache.get("0")
      @OptIn(ExperimentalCoroutinesApi::class) advanceTimeBy(25.hours)
      cache.get("0")
      coVerify(exactly = 2) { InMemoryFetcher::fetch.call(fetcher, "0", any<Continuation<Unit>>()) }
      coVerify(exactly = 2) {
        InMemoryStorage::store.call(
          storage,
          "0",
          InMemoryFetcher.FETCHED.first(),
          any<Continuation<Unit>>()
        )
      }
    }
  }

  @Test
  fun obtainsRememberedValueWhenItIsReadBeforeTimeToLive() {
    val fetcher = spyk(InMemoryFetcher())
    val storage = spyk(InMemoryStorage())
    val cache = cacheRule.cache.storingInto(storage).fetchingWith(fetcher).livingFor(1.days)
    coroutineScope.runTest {
      cache.get("0")
      @OptIn(ExperimentalCoroutinesApi::class) advanceTimeBy(23.hours)
      cache.get("0")
      coVerify(exactly = 1) { InMemoryFetcher::fetch.call(fetcher, "0", any<Continuation<Unit>>()) }
      coVerify(exactly = 1) { InMemoryStorage::get.call(storage, "0", any<Continuation<Unit>>()) }
    }
  }

  @Test
  fun remembersValueAgainWhenItIsObtainedAfterTimeToLive() {
    val fetcher = spyk(InMemoryFetcher())
    val storage = spyk(InMemoryStorage())
    val cache = cacheRule.cache.storingInto(storage).fetchingWith(fetcher).livingFor(1.days)
    coroutineScope.runTest {
      cache.get("0")
      @OptIn(ExperimentalCoroutinesApi::class) advanceTimeBy(25.hours)
      cache.get("0")
      coVerify(exactly = 2) { InMemoryFetcher::fetch.call(fetcher, "0", any<Continuation<Unit>>()) }
      coVerify(exactly = 2) {
        InMemoryStorage::store.call(
          storage,
          "0",
          InMemoryFetcher.FETCHED.first(),
          any<Continuation<Unit>>()
        )
      }
    }
  }
}

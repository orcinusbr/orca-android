package com.jeanbarrossilva.orca.core.feed.profile.toot.stat

import app.cash.turbine.test
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

internal class StatExtensionsTests {
    @Test
    fun buildsEmptyStat() {
        val stat = Stat<Int>()
        assertEquals(0, stat.count)
        runTest {
            stat.get(0).test {
                assertTrue(awaitItem().isEmpty())
                awaitComplete()
            }
        }
    }

    @Test
    fun buildsStatWithConfiguredCount() {
        assertEquals(0, Stat<Int>(count = 0) { }.count)
    }

    @Test
    fun buildsStatWithConfiguredGet() {
        runTest {
            assertEquals(
                listOf(0, 1),
                Stat(count = 2) {
                    get {
                        flowOf(listOf(0, 1))
                    }
                }
                    .get(0)
                    .first()
            )
        }
    }
}

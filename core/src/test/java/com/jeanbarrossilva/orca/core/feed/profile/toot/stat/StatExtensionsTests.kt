package com.jeanbarrossilva.orca.core.feed.profile.toot.stat

import com.jeanbarrossilva.orca.core.feed.profile.toot.stat.test.isEmpty
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

internal class StatExtensionsTests {
    @Test
    fun buildsEmptyStat() {
        val stat = emptyStat<Int>()
        assertEquals(0, stat.count)
        runTest { assertTrue(stat.get(0).isEmpty()) }
    }

    @Test
    fun buildsStatWithConfiguredCount() {
        assertEquals(0, buildStat<Int>(count = 0) { }.count)
    }

    @Test
    fun buildsStatWithConfiguredGet() {
        runTest {
            assertEquals(
                listOf(0, 1),
                buildStat(count = 2) {
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

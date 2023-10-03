package com.jeanbarrossilva.orca.platform.ui.core

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

internal class FlowExtensionsTests {
    @Test
    fun mapsEach() {
        runTest {
            flowOf(listOf(0, 1)).mapEach(2::times).test {
                assertEquals(listOf(0, 2), awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun flatMapsEach() {
        runTest {
            flowOf(listOf(0, 1)).flatMapEach(::flowOf).test {
                assertEquals(emptyList<Int>(), awaitItem())
                assertEquals(listOf(0), awaitItem())
                assertEquals(listOf(0, 1), awaitItem())
                awaitComplete()
            }
        }
    }
}

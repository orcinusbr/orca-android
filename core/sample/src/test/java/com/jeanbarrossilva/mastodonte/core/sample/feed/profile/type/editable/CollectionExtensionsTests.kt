package com.jeanbarrossilva.mastodonte.core.sample.feed.profile.type.editable

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith

internal class CollectionExtensionsTests {
    @Test
    fun `GIVEN a duplicate item WHEN replacing it once THEN it throws`() {
        assertFailsWith<IllegalStateException> {
            listOf("Hello", "Hello", "world").replacingOnceBy({ "Goodbye" }) {
                it == "Hello"
            }
        }
    }

    @Test
    fun `GIVEN an item WHEN replacing it once THEN it's replaced`() {
        assertContentEquals(
            listOf("Hello", "world"),
            listOf("Goodbye", "world").replacingOnceBy({ "Hello" }) { it == "Goodbye" }
        )
    }
}

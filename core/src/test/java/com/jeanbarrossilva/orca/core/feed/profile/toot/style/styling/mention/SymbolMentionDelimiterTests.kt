package com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class SymbolMentionDelimiterTests {
    @Test
    fun `GIVEN an alphabetical string that doesn't start with a mention symbol WHEN checking if it's a mention THEN it isn't`() { // ktlint-disable max-line-length
        assertFalse("john" matches SymbolMentionDelimiter.regex)
    }

    @Test
    fun `GIVEN an alphanumerical string that doesn't start with a mention symbol WHEN checking if it's a mention THEN it isn't`() { // ktlint-disable max-line-length
        assertFalse("john123" matches SymbolMentionDelimiter.regex)
    }

    @Test
    fun `GIVEN an illegal string that doesn't start with a mention symbol WHEN checking if it's a mention THEN it isn't`() { // ktlint-disable max-line-length
        assertFalse("!?#\$%^&*()" matches SymbolMentionDelimiter.regex)
    }

    @Test
    fun `GIVEN a string starting with a mention symbol with no following characters WHEN checking if it's a mention THEN it isn't`() { // ktlint-disable max-line-length
        assertFalse("@" matches SymbolMentionDelimiter.regex)
    }

    @Test
    fun `GIVEN a string starting with a mention symbol with illegal characters WHEN checking if it's a mention THEN it isn't`() { // ktlint-disable max-line-length
        assertFalse("@!?#$%^&*()" matches SymbolMentionDelimiter.regex)
    }

    @Test
    fun `GIVEN a string starting with a mention symbol with multiple following alphabetical characters WHEN checking if it's a mention THEN it is`() { // ktlint-disable max-line-length
        assertTrue("@john" matches SymbolMentionDelimiter.regex)
    }

    @Test
    fun `GIVEN a string starting with a mention symbol with multiple alphanumerical characters WHEN checking if it's a mention THEN it is`() { // ktlint-disable max-line-length
        assertTrue("@john123" matches SymbolMentionDelimiter.regex)
    }
}

package com.jeanbarrossilva.mastodonte.core.toot

import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class AccountTests {
    @Test
    fun `GIVEN a blank username WHEN creating an account with it THEN it throws`() {
        assertFailsWith<IllegalArgumentException> {
            " " at "appleseed.com"
        }
    }

    @Test
    fun `GIVEN a username with an @ WHEN creating an account with it THEN it throws`() {
        assertFailsWith<IllegalArgumentException> {
            "john@" at "appleseed.com"
        }
    }

    @Test
    fun `GIVEN a blank instance WHEN creating an account with it THEN it throws`() {
        assertFailsWith<IllegalArgumentException> {
            "john" at " "
        }
    }

    @Test
    fun `GIVEN an instance with an @ WHEN creating an account with it THEN it throws`() {
        assertFailsWith<IllegalArgumentException> {
            "john" at "@appleseed.com"
        }
    }
}

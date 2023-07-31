package com.jeanbarrossilva.mastodonte.core.feed.profile.account

import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class StringExtensionsTests {
    @Test
    fun `GIVEN a blank username WHEN creating an account with it THEN it throws`() {
        assertFailsWith<Account.BlankUsernameException> {
            " " at "appleseed.com"
        }
    }

    @Test
    fun `GIVEN a username with an illegal character WHEN creating an account with it THEN it throws`() { // ktlint-disable max-line-length
        assertFailsWith<Account.IllegalUsernameException> {
            "john@" at "appleseed.com"
        }
    }

    @Test
    fun `GIVEN a blank instance WHEN creating an account with it THEN it throws`() {
        assertFailsWith<Account.BlankInstanceException> {
            "john" at " "
        }
    }

    @Test
    fun `GIVEN an instance with an illegal character WHEN creating an account with it THEN it throws`() { // ktlint-disable max-line-length
        assertFailsWith<Account.IllegalInstanceException> {
            "john" at "@appleseed.com"
        }
    }

    @Test
    fun `GIVEN an instance without a TLD WHEN creating an account with it THEN it throws`() {
        assertFailsWith<Account.InstanceWithoutDomainException> {
            "john" at "appleseed."
        }
    }

    @Test
    fun `GIVEN a valid username and a valid instance WHEN creating an account with them THEN it's created`() { // ktlint-disable max-line-length
        "john" at "appleseed.com"
    }
}

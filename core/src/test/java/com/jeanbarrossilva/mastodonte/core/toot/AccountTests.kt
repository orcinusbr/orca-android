package com.jeanbarrossilva.mastodonte.core.toot

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

internal class AccountTests {
    @Test
    fun `GIVEN a blank username WHEN verifying if it's valid THEN it isn't`() {
        assertFalse(Account.isUsernameValid(" "))
    }

    @Test
    fun `GIVEN a username with an @ WHEN verifying if it's valid THEN it isn't`() {
        assertFalse(Account.isUsernameValid("john@"))
    }

    @Test
    fun `GIVEN a blank instance WHEN verifying if it's valid THEN it isn't`() {
        assertFalse(Account.isInstanceValid(" "))
    }

    @Test
    fun `GIVEN an instance with an @ WHEN verifying if it's valid THEN it isn't`() {
        assertFalse(Account.isInstanceValid("@appleseed.com"))
    }

    @Test
    fun `GIVEN a blank username WHEN creating an account with it THEN it throws`() {
        assertFailsWith<Account.BlankUsernameException> {
            " " at "appleseed.com"
        }
    }

    @Test
    fun `GIVEN a username with an @ WHEN creating an account with it THEN it throws`() {
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
    fun `GIVEN an instance with an @ WHEN creating an account with it THEN it throws`() {
        assertFailsWith<Account.IllegalInstanceException> {
            "john" at "@appleseed.com"
        }
    }

    @Test
    fun `GIVEN an instance without a TLD WHEN creating an account with it THEN it throws`() {
        assertFailsWith<Account.InvalidInstanceTldException> {
            "john" at "appleseed."
        }
    }
}

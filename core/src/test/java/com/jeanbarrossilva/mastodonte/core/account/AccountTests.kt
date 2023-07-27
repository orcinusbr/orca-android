package com.jeanbarrossilva.mastodonte.core.account

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

internal class AccountTests {
    @Test
    fun `GIVEN a blank string WHEN parsing it THEN it throws`() {
        assertFailsWith<Account.Companion.BlankStringException> {
            Account.of(" ")
        }
    }

    @Test
    fun `GIVEN a blank string with a fallback instance WHEN parsing it THEN it throws`() {
        assertFailsWith<Account.Companion.BlankStringException> {
            Account.of(" ", "appleseed.com")
        }
    }

    @Test
    fun `GIVEN a string containing a username with illegal characters WHEN parsing it THEN it throws`() { // ktlint-disable max-line-length
        assertFailsWith<Account.IllegalUsernameException> {
            Account.of("john @appleseed.com")
        }
    }

    @Test
    fun `GIVEN a string containing only a username without the separator without a fallback instance WHEN parsing it THEN it throws`() { // ktlint-disable max-line-length
        assertFailsWith<Account.BlankInstanceException> {
            Account.of("john")
        }
    }

    @Test
    fun `GIVEN a string containing only a username with the separator without a fallback instance WHEN parsing it THEN it throws`() { // ktlint-disable max-line-length
        assertFailsWith<Account.BlankInstanceException> {
            Account.of("john@")
        }
    }

    @Test
    fun `GIVEN a string containing a valid username and an instance with illegal characters WHEN parsing it THEN it throws`() { // ktlint-disable max-line-length
        assertFailsWith<Account.IllegalInstanceException> {
            Account.of("john@apple seed.com")
        }
    }

    @Test
    fun `GIVEN a string containing a valid username without an instance with a fallback one with illegal characters WHEN parsing it THEN it throws`() { // ktlint-disable max-line-length
        assertFailsWith<Account.IllegalInstanceException> {
            Account.of("john", fallbackInstance = "apple seed.com")
        }
    }

    @Test
    fun `GIVEN a string containing a valid username and a valid instance WHEN parsing it THEN it creates an account`() { // ktlint-disable max-line-length
        assertEquals("john" at "appleseed.com", Account.of("john@appleseed.com"))
    }

    @Test
    fun `GIVEN a string containing only a username with a valid fallback instance WHEN parsing it THEN it creates an account`() { // ktlint-disable max-line-length
        assertEquals(
            "john" at "appleseed.com",
            Account.of("john", fallbackInstance = "appleseed.com")
        )
    }

    @Test
    fun `GIVEN a blank username WHEN verifying if it's valid THEN it isn't`() {
        assertFalse(Account.isUsernameValid(" "))
    }

    @Test
    fun `GIVEN a username with an illegal character WHEN verifying if it's valid THEN it isn't`() {
        assertFalse(Account.isUsernameValid("john@"))
    }

    @Test
    fun `GIVEN a blank instance WHEN verifying if it's valid THEN it isn't`() {
        assertFalse(Account.isInstanceValid(" "))
    }

    @Test
    fun `GIVEN an instance with an illegal character WHEN verifying if it's valid THEN it isn't`() {
        assertFalse(Account.isInstanceValid("@appleseed.com"))
    }

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

package com.jeanbarrossilva.orca.core.feed.profile.account

import com.jeanbarrossilva.orca.core.instance.domain.Domain
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
    fun `GIVEN a blank domain WHEN creating an account with it THEN it throws`() {
        assertFailsWith<Domain.BlankValueException> {
            "john" at " "
        }
    }

    @Test
    fun `GIVEN an domain with an illegal character WHEN creating an account with it THEN it throws`() { // ktlint-disable max-line-length
        assertFailsWith<Domain.IllegalValueException> {
            "john" at "@appleseed.com"
        }
    }

    @Test
    fun `GIVEN an domain without a TLD WHEN creating an account with it THEN it throws`() {
        assertFailsWith<Domain.ValueWithoutTopLevelDomainException> {
            "john" at "appleseed."
        }
    }

    @Test
    fun `GIVEN a valid username and a valid domain WHEN creating an account with them THEN it's created`() { // ktlint-disable max-line-length
        "john" at "appleseed.com"
    }
}

/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.feed.profile.account

import com.jeanbarrossilva.orca.core.instance.domain.Domain
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

internal class AccountTests {
  @Test
  fun `GIVEN a blank string WHEN parsing it THEN it throws`() {
    assertFailsWith<Account.Companion.BlankStringException> { Account.of(" ") }
  }

  @Test
  fun `GIVEN a blank string with a fallback domain WHEN parsing it THEN it throws`() {
    assertFailsWith<Account.Companion.BlankStringException> { Account.of(" ", "appleseed.com") }
  }

  @Test
  fun `GIVEN a string containing a username with illegal characters WHEN parsing it THEN it throws`() {
    assertFailsWith<Account.IllegalUsernameException> { Account.of("john @appleseed.com") }
  }

  @Test
  fun `GIVEN a string containing only a username without the separator without a fallback domain WHEN parsing it THEN it throws`() {
    assertFailsWith<Domain.BlankValueException> { Account.of("john") }
  }

  @Test
  fun `GIVEN a string containing only a username with the separator without a fallback domain WHEN parsing it THEN it throws`() {
    assertFailsWith<Domain.BlankValueException> { Account.of("john@") }
  }

  @Test
  fun `GIVEN a string containing a valid username and an domain with illegal characters WHEN parsing it THEN it throws`() {
    assertFailsWith<Domain.IllegalValueException> { Account.of("john@apple seed.com") }
  }

  @Test
  fun `GIVEN a string containing a valid username without an domain with a fallback one with illegal characters WHEN parsing it THEN it throws`() {
    assertFailsWith<Domain.IllegalValueException> {
      Account.of("john", fallbackDomain = "apple seed.com")
    }
  }

  @Test
  fun `GIVEN a string containing a valid username and a valid domain WHEN parsing it THEN it creates an account`() {
    assertEquals("john" at "appleseed.com", Account.of("john@appleseed.com"))
  }

  @Test
  fun `GIVEN a string containing only a username with a valid fallback domain WHEN parsing it THEN it creates an account`() {
    assertEquals("john" at "appleseed.com", Account.of("john", fallbackDomain = "appleseed.com"))
  }

  @Test
  fun `GIVEN a blank username WHEN verifying if it's valid THEN it isn't`() {
    assertFalse(Account.isUsernameValid(" "))
  }

  @Test
  fun `GIVEN a username with an illegal character WHEN verifying if it's valid THEN it isn't`() {
    assertFalse(Account.isUsernameValid("john@"))
  }
}

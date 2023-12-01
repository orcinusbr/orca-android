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
import kotlin.test.assertFailsWith

internal class StringExtensionsTests {
  @Test
  fun `GIVEN a blank username WHEN creating an account with it THEN it throws`() {
    assertFailsWith<Account.BlankUsernameException> { " " at "appleseed.com" }
  }

  @Test
  fun `GIVEN a username with an illegal character WHEN creating an account with it THEN it throws`() {
    assertFailsWith<Account.IllegalUsernameException> { "john@" at "appleseed.com" }
  }

  @Test
  fun `GIVEN a blank domain WHEN creating an account with it THEN it throws`() {
    assertFailsWith<Domain.BlankValueException> { "john" at " " }
  }

  @Test
  fun `GIVEN an domain with an illegal character WHEN creating an account with it THEN it throws`() {
    assertFailsWith<Domain.IllegalValueException> { "john" at "@appleseed.com" }
  }

  @Test
  fun `GIVEN an domain without a TLD WHEN creating an account with it THEN it throws`() {
    assertFailsWith<Domain.ValueWithoutTopLevelDomainException> { "john" at "appleseed." }
  }

  @Test
  fun `GIVEN a valid username and a valid domain WHEN creating an account with them THEN it's created`() {
    "john" at "appleseed.com"
  }
}

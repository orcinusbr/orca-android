/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.feed.profile.account

import br.com.orcinus.orca.core.instance.domain.Domain
import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class DomainTests {
  @Test
  fun `GIVEN a blank domain WHEN ensuring its integrity THEN it throws`() {
    assertFailsWith<Domain.BlankValueException> { Domain(" ") }
  }

  @Test
  fun `GIVEN an domain with an illegal character WHEN ensuring its integrity THEN it throws`() {
    assertFailsWith<Domain.IllegalValueException> { Domain("@appleseed.com") }
  }

  @Test
  fun `GIVEN an domain without a TLD WHEN ensuring its integrity THEN it throws`() {
    assertFailsWith<Domain.ValueWithoutTopLevelDomainException> { Domain("appleseed.") }
  }
}

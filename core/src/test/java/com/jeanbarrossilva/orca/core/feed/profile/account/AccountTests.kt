/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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

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

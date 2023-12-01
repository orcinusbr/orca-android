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

package com.jeanbarrossilva.orca.core.feed.profile.type.followable

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class FollowTests {
  @Test
  fun `GIVEN a blank string WHEN parsing it into a follow status THEN it throws`() {
    assertFailsWith<Follow.Companion.BlankStringException> { Follow.of(" ") }
  }

  @Test
  fun `GIVEN an invalid string WHEN parsing it into a follow status THEN it throws`() {
    assertFailsWith<Follow.Companion.InvalidFollowString> { Follow.of("🥸") }
  }

  @Test
  fun `GIVEN a public unfollowed status string WHEN parsing it THEN it returns the status`() {
    assertEquals(Follow.Public.unfollowed(), Follow.of("${Follow.Public.unfollowed()}"))
  }

  @Test
  fun `GIVEN a public following status string WHEN parsing it THEN it returns the status`() {
    assertEquals(Follow.Public.following(), Follow.of("${Follow.Public.following()}"))
  }

  @Test
  fun `GIVEN a private unfollowed status string WHEN parsing it THEN it returns the status`() {
    assertEquals(Follow.Private.unfollowed(), Follow.of("${Follow.Private.unfollowed()}"))
  }

  @Test
  fun `GIVEN a private requested status string WHEN parsing it THEN it returns the status`() {
    assertEquals(Follow.Private.requested(), Follow.of("${Follow.Private.requested()}"))
  }

  @Test
  fun `GIVEN a private following status string WHEN parsing it THEN it returns the status`() {
    assertEquals(Follow.Private.following(), Follow.of("${Follow.Private.following()}"))
  }

  @Test
  fun `GIVEN a public unfollowed status WHEN toggling it THEN it's followed`() {
    assertEquals(Follow.Public.following(), Follow.Public.unfollowed().toggled())
  }

  @Test
  fun `GIVEN a public followed status WHEN toggling it THEN it's unfollowed`() {
    assertEquals(Follow.Public.unfollowed(), Follow.Public.following().toggled())
  }

  @Test
  fun `GIVEN a private unfollowed status WHEN toggling it THEN it's requested`() {
    assertEquals(Follow.Private.requested(), Follow.Private.unfollowed().toggled())
  }

  @Test
  fun `GIVEN a private requested status WHEN toggling it THEN it's unfollowed`() {
    assertEquals(Follow.Private.unfollowed(), Follow.Private.requested().toggled())
  }

  @Test
  fun `GIVEN a cohesive status WHEN requiring it to be cohesive THEN it is`() {
    Follow.requireVisibilityMatch(Follow.Public.unfollowed(), Follow.Public.following())
  }

  @Test
  fun `GIVEN a non-cohesive status WHEN requiring it to be cohesive THEN it throws`() {
    assertFailsWith<IllegalArgumentException> {
      Follow.requireVisibilityMatch(Follow.Public.unfollowed(), Follow.Private.unfollowed())
    }
  }
}

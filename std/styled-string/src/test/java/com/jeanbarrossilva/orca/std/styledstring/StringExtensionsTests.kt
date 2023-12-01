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

package com.jeanbarrossilva.orca.std.styledstring

import assertk.assertThat
import assertk.assertions.containsExactly
import com.jeanbarrossilva.orca.std.styledstring.style.type.Email
import kotlin.test.Test

internal class StringExtensionsTests {
  @Test
  fun transformsEachRegexMatchingPortion() {
    assertThat(
        "me@jeanbarrossilva.com, john@appleseed.com".map(Email.regex) { indices, _ -> indices }
      )
      .containsExactly(0..21, 24..41)
  }
}

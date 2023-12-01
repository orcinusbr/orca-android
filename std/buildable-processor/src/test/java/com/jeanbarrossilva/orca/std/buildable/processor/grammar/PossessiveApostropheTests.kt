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

package com.jeanbarrossilva.orca.std.buildable.processor.grammar

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

internal class PossessiveApostropheTests {
  @Test
  fun hasTrailingSWhenStringIsEmpty() {
    assertThat(PossessiveApostrophe.of("")).isEqualTo(PossessiveApostrophe.WITH_TRAILING_S)
  }

  @Test
  fun hasTrailingSWhenStringIsBlank() {
    assertThat(PossessiveApostrophe.of(" ")).isEqualTo(PossessiveApostrophe.WITH_TRAILING_S)
  }

  @Test
  fun hasTrailingSWhenStringDoesNotEndWithS() {
    assertThat(PossessiveApostrophe.of("Jean")).isEqualTo(PossessiveApostrophe.WITH_TRAILING_S)
  }

  @Test
  fun doesNotHaveTrailingSWhenStringEndsWithS() {
    assertThat(PossessiveApostrophe.of("jeans")).isEqualTo(PossessiveApostrophe.WITHOUT_TRAILING_S)
  }
}

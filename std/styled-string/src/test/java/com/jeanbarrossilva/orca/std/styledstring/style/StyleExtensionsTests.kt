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

package com.jeanbarrossilva.orca.std.styledstring.style

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.jeanbarrossilva.orca.std.styledstring.style.type.Bold
import kotlin.test.Test

internal class StyleExtensionsTests {
  @Test
  fun isWithin() {
    assertThat(Bold(0..4).isWithin("Hello!"))
  }

  @Test
  fun isNotWithin() {
    assertThat(Bold(0..6).isWithin("Hello."))
  }

  @Test
  fun isChopped() {
    assertThat(Bold(0..Int.MAX_VALUE).isChoppedBy("🦩")).isTrue()
  }

  @Test
  fun isNotChopped() {
    assertThat(Bold(0..4).isChoppedBy("Hello, world!")).isFalse()
  }
}

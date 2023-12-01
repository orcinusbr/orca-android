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
import kotlin.test.Test

internal class IterableExtensionsTests {
  @Test
  fun mapsConditionally() {
    assertThat(listOf(0, 1, 2, 3, 4).map({ it % 2 == 0 }) { it * it })
      .containsExactly(0, 1, 4, 3, 16)
  }
}

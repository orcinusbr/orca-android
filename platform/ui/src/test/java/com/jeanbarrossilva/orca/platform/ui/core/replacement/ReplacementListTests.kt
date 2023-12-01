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

package com.jeanbarrossilva.orca.platform.ui.core.replacement

import org.junit.Assert.assertEquals
import org.junit.Test

internal class ReplacementListTests {
  @Test
  fun adds() {
    assertEquals(replacementListOf(0), emptyReplacementList<Int>().apply { add(0) })
  }

  @Test
  fun replaces() {
    assertEquals(
      replacementListOf("Hello,", "world!", selector = String::first),
      replacementListOf("Hey,", "world!", selector = String::first).apply { add("Hello,") }
    )
  }
}

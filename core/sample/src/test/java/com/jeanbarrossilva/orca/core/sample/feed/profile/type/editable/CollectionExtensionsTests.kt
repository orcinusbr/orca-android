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

package com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith

internal class CollectionExtensionsTests {
  @Test
  fun `GIVEN a duplicate item WHEN replacing it once THEN it throws`() {
    assertFailsWith<IllegalStateException> {
      listOf("Hello", "Hello", "world").replacingOnceBy({ "Goodbye" }) { it == "Hello" }
    }
  }

  @Test
  fun `GIVEN an item WHEN replacing it once THEN it's replaced`() {
    assertContentEquals(
      listOf("Hello", "world"),
      listOf("Goodbye", "world").replacingOnceBy({ "Hello" }) { it == "Goodbye" }
    )
  }
}

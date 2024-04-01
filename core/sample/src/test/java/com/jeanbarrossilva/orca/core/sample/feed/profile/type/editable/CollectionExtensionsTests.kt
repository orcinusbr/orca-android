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

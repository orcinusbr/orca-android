/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.std.injector.module.replacement

import assertk.assertThat
import assertk.assertions.containsExactly
import kotlin.test.Test

internal class DelegatorMutableReplacementListTests {
  @Test
  fun createsReplacementListWithDefaultSelector() {
    assertThat(mutableReplacementListOf(0, 1).apply { add(0) }).containsExactly(0, 1)
  }

  @Test
  fun createsReplacementListWithCustomSelector() {
    assertThat(
        mutableReplacementListOf(0, 1, 2) { if (it == 0) Object() else it }
          .apply { addAll(arrayOf(0, 1)) }
      )
      .containsExactly(0, 1, 2, 0)
  }
}

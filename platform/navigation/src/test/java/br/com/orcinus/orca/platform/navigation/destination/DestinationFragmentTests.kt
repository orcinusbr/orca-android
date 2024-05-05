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

package br.com.orcinus.orca.platform.navigation.destination

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

internal class DestinationFragmentTests {
  @Test
  fun identifies() {
    assertThat(
        object : DestinationFragment({ android.R.id.empty }) {}.apply(DestinationFragment::setId).id
      )
      .isEqualTo(android.R.id.empty)
  }
}

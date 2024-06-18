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

package br.com.orcinus.orca.std.buildable.processor.grammar

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

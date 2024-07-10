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

package br.com.orcinus.orca.std.markdown.style

import assertk.assertThat
import assertk.assertions.containsExactly
import kotlin.test.Test

internal class IterableExtensionsTests {
  @Test
  fun mergesStyles() {
    assertThat(setOf(Style.Bold(0..2), Style.Bold(2..8), Style.Italic(9..12)).merge())
      .containsExactly(Style.Bold(0..8), Style.Italic(9..12))
  }

  @Test
  fun sortsMergedStyles() {
    assertThat(setOf(Style.Italic(9..12), Style.Bold(4..8), Style.Bold(2..4)).merge())
      .containsExactly(Style.Bold(2..8), Style.Italic(9..12))
  }

  @Test
  fun returnsReceiverStylesWhenTrimmingWithEmptyIndices() {
    assertThat(setOf(Style.Bold(0..8), Style.Italic(12..24)) - { IntRange.EMPTY })
      .containsExactly(Style.Bold(0..8), Style.Italic(12..24))
  }

  @Test
  fun removesStyles() {
    assertThat(setOf(Style.Bold(0..8), Style.Italic(12..24)) - { 4..16 })
      .containsExactly(Style.Bold(0..3), Style.Italic(17..24))
  }
}

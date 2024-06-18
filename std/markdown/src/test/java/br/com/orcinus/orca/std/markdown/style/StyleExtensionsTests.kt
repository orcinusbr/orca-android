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
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.Test

internal class StyleExtensionsTests {
  @Test
  fun isWithin() {
    assertThat(Style.Bold(0..4).isWithin("Hello!")).isTrue()
  }

  @Test
  fun isNotWithin() {
    assertThat(Style.Bold(0..6).isWithin("Hello.")).isFalse()
  }

  @Test
  fun isChopped() {
    assertThat(Style.Bold(0..Int.MAX_VALUE).isChoppedBy("🦩")).isTrue()
  }

  @Test
  fun isNotChopped() {
    assertThat(Style.Bold(0..4).isChoppedBy("Hello, world!")).isFalse()
  }
}

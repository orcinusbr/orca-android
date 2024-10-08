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

package br.com.orcinus.orca.ext.reflection.java

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.Test

internal class AccessibleObjectExtensionsTests {
  @Test
  fun accesses() {
    assertThat(
        object {
            @Suppress("unused") private var msg = "🍿🎁👩🏻‍💻"
          }::class
          .java
          .getDeclaredField("msg")
          .access { @Suppress("DEPRECATION") isAccessible }
      )
      .isTrue()
  }

  @Test
  fun changesAccessibilityToPreviousOneAfterAccessing() {
    @Suppress("DEPRECATION")
    assertThat(
        object {
            @Suppress("unused") private val msg = "⌛️"
          }::class
          .java
          .getDeclaredField("msg")
          .access { this }
          .isAccessible
      )
      .isFalse()
  }
}

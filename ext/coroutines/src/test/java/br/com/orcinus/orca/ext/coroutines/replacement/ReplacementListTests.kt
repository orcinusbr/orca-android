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

package br.com.orcinus.orca.ext.coroutines.replacement

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsAll
import assertk.assertions.containsExactly
import assertk.assertions.doesNotContain
import kotlin.test.Test
import org.opentest4j.AssertionFailedError

internal class ReplacementListTests {
  @Test
  fun contains() {
    assertThat(replacementListOf(":P", ";P", selector = String::first)).contains(":)")
  }

  @Test
  fun doesNotContain() {
    assertThat(replacementListOf(":P", ":)", selector = String::first)).doesNotContain(";P")
  }

  @Test
  fun containsAll() {
    assertThat(replacementListOf(":P", ":)", selector = String::first)).containsAll(":T", ":3")
  }

  @Test(expected = AssertionFailedError::class)
  fun doesNotContainAll() {
    assertThat(replacementListOf(":P", ":)", selector = String::first)).containsAll(":P", ";(")
  }

  @Test
  fun adds() {
    assertThat(replacementListOf<Int>().apply { add(0) }).containsExactly(0)
  }

  @Test
  fun replaces() {
    assertThat(
        replacementListOf("Hey,", "world!", selector = String::first).apply { add("Hello,") }
      )
      .containsExactly("Hello,", "world!")
  }
}
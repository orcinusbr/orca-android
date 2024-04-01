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

package br.com.orcinus.orca.platform.ime

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.Test

internal class ImeTests {
  @Test
  fun visibilityIsUnknown() {
    assertThat(Ime.Unknown.hasUnknownVisibility).isTrue()
  }

  @Test
  fun visibilityIsKnownWhenOpen() {
    assertThat(Ime.Open.hasUnknownVisibility).isFalse()
  }

  @Test
  fun visibilityIsNotKnownWhenClosed() {
    assertThat(Ime.Closed.hasUnknownVisibility).isFalse()
  }

  @Test
  fun isClosed() {
    assertThat(Ime.Closed.isClosed).isTrue()
  }

  @Test
  fun isNotClosedWhenUnknown() {
    assertThat(Ime.Unknown.isClosed).isFalse()
  }

  @Test
  fun isNotClosedWhenOpen() {
    assertThat(Ime.Open.isClosed).isFalse()
  }

  @Test
  fun isOpen() {
    assertThat(Ime.Open.isOpen).isTrue()
  }

  @Test
  fun isNotOpenWhenUnknown() {
    assertThat(Ime.Unknown.isOpen).isFalse()
  }

  @Test
  fun isNotOpenWhenClosed() {
    assertThat(Ime.Closed.isOpen).isFalse()
  }
}

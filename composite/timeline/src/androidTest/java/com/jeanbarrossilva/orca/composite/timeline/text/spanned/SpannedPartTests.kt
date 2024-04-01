/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.orca.composite.timeline.text.spanned

import android.graphics.Typeface
import android.text.style.StyleSpan
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import assertk.assertions.prop
import org.junit.Test

internal class SpannedPartTests {
  @Test
  fun comparesEqualSpannedParts() {
    assertThat(Part(0..8).span(StyleSpan(Typeface.NORMAL)))
      .isEqualTo(Part(0..8).span(StyleSpan(Typeface.NORMAL)))
  }

  @Test
  fun comparesSpannedPartsDifferingInIndices() {
    assertThat(Part(0..8).span(StyleSpan(Typeface.NORMAL)))
      .isNotEqualTo(Part(1..9).span(StyleSpan(Typeface.NORMAL)))
  }

  @Test
  fun comparesSpannedPartsDifferingInSpans() {
    assertThat(Part(0..8).span(StyleSpan(Typeface.NORMAL)))
      .isNotEqualTo(Part(0..8).span(StyleSpan(Typeface.BOLD)))
  }

  @Test
  fun spans() {
    val span = StyleSpan(Typeface.NORMAL)
    assertThat(Part(0..8).span(span)).all {
      prop(Part.Spanned::getIndices).isEqualTo(0..8)
      prop(Part.Spanned::getSpans).isEqualTo(listOf(span))
    }
  }
}

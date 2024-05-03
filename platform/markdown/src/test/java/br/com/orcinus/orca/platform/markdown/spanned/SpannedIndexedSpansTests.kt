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

package br.com.orcinus.orca.platform.markdown.spanned

import android.graphics.Typeface
import android.text.style.StyleSpan
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import assertk.assertions.prop
import br.com.orcinus.orca.platform.testing.context
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SpannedIndexedSpansTests {
  @Test
  fun comparesEqualSpannedParts() {
    assertThat(IndexedSpans(context, 0..8, StyleSpan(Typeface.NORMAL)))
      .isEqualTo(IndexedSpans(context, 0..8, StyleSpan(Typeface.NORMAL)))
  }

  @Test
  fun comparesSpannedPartsDifferingInIndices() {
    assertThat(IndexedSpans(context, 0..8, StyleSpan(Typeface.NORMAL)))
      .isNotEqualTo(IndexedSpans(context, 1..9, StyleSpan(Typeface.NORMAL)))
  }

  @Test
  fun comparesSpannedPartsDifferingInSpans() {
    assertThat(IndexedSpans(context, 0..8, StyleSpan(Typeface.NORMAL)))
      .isNotEqualTo(IndexedSpans(context, 0..8, StyleSpan(Typeface.BOLD)))
  }

  @Test
  fun spans() {
    val span = StyleSpan(Typeface.NORMAL)
    assertThat(IndexedSpans(context, 0..8, span)).all {
      prop(IndexedSpans::getIndices).isEqualTo(0..8)
      prop(IndexedSpans::getSpans).isEqualTo(listOf(span))
    }
  }
}

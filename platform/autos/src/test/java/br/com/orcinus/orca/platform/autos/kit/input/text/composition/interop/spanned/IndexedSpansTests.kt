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

package br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.spanned

import android.graphics.Typeface
import android.text.style.StyleSpan
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import br.com.orcinus.orca.platform.testing.context
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class IndexedSpansTests {
  @Test
  fun comparesEqualEmptyIndexedSpans() {
    assertThat(IndexedSpans(context, 0..8, emptyList())).isEqualTo(IndexedSpans(context, 0..8))
  }

  @Test
  fun comparesDifferentIndexedSpans() {
    assertThat(IndexedSpans(context, 0..8)).isNotEqualTo(IndexedSpans(context, 1..9))
  }

  @Test
  fun comparesEqualPopulatedIndexedSpans() {
    assertThat(IndexedSpans(context, 0..8, StyleSpan(Typeface.NORMAL)))
      .isEqualTo(IndexedSpans(context, 0..8, StyleSpan(Typeface.NORMAL)))
  }

  @Test
  fun comparesPopulatedIndexedSpansDifferingInIndices() {
    assertThat(IndexedSpans(context, 0..8, StyleSpan(Typeface.NORMAL)))
      .isNotEqualTo(IndexedSpans(context, 1..9, StyleSpan(Typeface.NORMAL)))
  }

  @Test
  fun comparesPopulatedIndexedSpansDifferingInSpans() {
    assertThat(IndexedSpans(context, 0..8, StyleSpan(Typeface.NORMAL)))
      .isNotEqualTo(IndexedSpans(context, 0..8, StyleSpan(Typeface.BOLD)))
  }

  @Test
  fun copies() {
    assertThat(
        IndexedSpans(context, 0..8, StyleSpan(Typeface.NORMAL))
          .copy(listOf(StyleSpan(Typeface.BOLD)))
      )
      .isEqualTo(IndexedSpans(context, 0..8, StyleSpan(Typeface.BOLD)))
  }
}

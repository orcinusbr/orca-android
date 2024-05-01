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

package br.com.orcinus.orca.platform.markdown.spanned.span

import android.graphics.Typeface
import android.text.style.StyleSpan
import android.text.style.URLSpan
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import br.com.orcinus.orca.std.uri.URIBuilder
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ParcelableSpanExtensionsTests {
  private val uri = URIBuilder.url().scheme("https").host("orca.orcinus.com.br").build()

  @Test
  fun structurallyComparesEqualStyleSpans() {
    assertThat(StyleSpan(Typeface.NORMAL).isStructurallyEqualTo(StyleSpan(Typeface.NORMAL)))
      .isTrue()
  }

  @Test
  fun structurallyComparesDifferentStyleSpans() {
    assertThat(StyleSpan(Typeface.NORMAL).isStructurallyEqualTo(StyleSpan(Typeface.BOLD))).isFalse()
  }

  @Test
  fun structurallyComparesEqualURLSpans() {
    assertThat(URLSpan("$uri").isStructurallyEqualTo(URLSpan("$uri"))).isTrue()
  }

  @Test
  fun structurallyComparesDifferentURLSpans() {
    assertThat(
        URLSpan("$uri")
          .isStructurallyEqualTo(
            URLSpan("${URIBuilder.url().scheme("https").host("beluga.orcinus.com.br").build()}")
          )
      )
      .isFalse()
  }
}

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
import android.text.Html
import android.text.style.StyleSpan
import assertk.assertThat
import assertk.assertions.containsExactly
import br.com.orcinus.orca.platform.markdown.spanned.span.createStyleSpan
import br.com.orcinus.orca.platform.testing.context
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SpannedExtensionsTests {
  @Test
  fun partitions() {
    assertThat(
        Html.fromHtml("<p><b><i>Hello</i></b>, <i>world</i>!</p>", Html.FROM_HTML_MODE_COMPACT)
          .getParts(context)
      )
      .containsExactly(
        Part(0..4).span(context, StyleSpan(Typeface.ITALIC)),
        Part(0..4).span(context, createStyleSpan(Typeface.BOLD, fontWeightAdjustment = 0)),
        Part(5..6),
        Part(7..11).span(context, StyleSpan(Typeface.ITALIC))
      )
  }
}

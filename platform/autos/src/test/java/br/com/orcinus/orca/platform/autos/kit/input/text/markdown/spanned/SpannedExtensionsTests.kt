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

package br.com.orcinus.orca.platform.autos.kit.input.text.markdown.spanned

import android.graphics.Typeface
import android.text.Html
import android.text.style.StyleSpan
import assertk.assertThat
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.areStructurallyEqual
import br.com.orcinus.orca.platform.testing.context
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SpannedExtensionsTests {
  @Test
  fun getsIndexedSpans() {
    assertThat(
        Html.fromHtml("<p><b><i>Hello</i></b>, <i>world</i>!</p>", Html.FROM_HTML_MODE_COMPACT)
          .getIndexedSpans(context)
      )
      .areStructurallyEqual(
        IndexedSpans(context, 0..5, StyleSpan(Typeface.ITALIC), StyleSpan(Typeface.BOLD)),
        IndexedSpans(context, 7..12, StyleSpan(Typeface.ITALIC))
      )
  }
}

/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.composite.timeline.composition.interop.spanned

import android.graphics.Typeface
import android.text.style.StyleSpan
import androidx.core.text.getSpans
import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.composite.timeline.composition.interop.annotated.areStructurallyEqual
import br.com.orcinus.orca.composite.timeline.composition.interop.spanned.span.areStructurallyEqual
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.buildMarkdown
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class MarkdownExtensionsTests {
  @Test
  fun editableContainsSameTextAsTheMarkdownOnWhichItIsBased() {
    assertThat(
        buildMarkdown {
            bold { +':' }
            italic { +'p' }
          }
          .toEditable(context)
          .toString()
      )
      .isEqualTo(":p")
  }

  @Test
  fun spansMarkdownEditable() {
    assertThat(
        buildMarkdown {
            bold { +":)" }
            +' '
            italic { +":D" }
          }
          .toEditable(context)
          .getIndexedSpans(context)
      )
      .areStructurallyEqual(
        IndexedSpans(context, 0..2, StyleSpan(Typeface.BOLD)),
        IndexedSpans(context, 3..5, StyleSpan(Typeface.ITALIC))
      )
  }

  @Test
  fun setsMarkdownEditableSpans() {
    assertThat(
        Markdown.unstyled("Hello!")
          .toEditable(context)
          .apply { setSpan(StyleSpan(Typeface.BOLD), 0, 5, 0) }
          .getSpans<StyleSpan>(0, 5)
      )
      .areStructurallyEqual(StyleSpan(Typeface.BOLD))
  }

  @Test
  fun unsetsMarkdownEditableSpans() {
    assertThat(
        buildMarkdown {
            bold { +"Hello" }
            italic { +'!' }
          }
          .toEditable(context)
          .apply { setSpan(null, 0, 5, 0) }
          .getIndexedSpans(context)
      )
      .areStructurallyEqual(IndexedSpans(context, 5..6, StyleSpan(Typeface.ITALIC)))
  }
}

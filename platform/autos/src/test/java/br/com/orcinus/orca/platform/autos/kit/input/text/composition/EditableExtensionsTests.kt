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

package br.com.orcinus.orca.platform.autos.kit.input.text.composition

import android.graphics.Typeface
import android.text.Editable
import android.text.style.StyleSpan
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.spanned.IndexedSpans
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.spanned.getIndexedSpans
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.buildMarkdown
import io.mockk.mockk
import kotlin.test.Test
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class EditableExtensionsTests {
  @Test
  fun editableIsBasedOnMarkdown() {
    runTest {
      assertThat(Markdown.empty.toEditableAsFlow(context).single().isBasedOnMarkdown).isTrue()
    }
  }

  @Test
  fun editableIsNotBasedOnMarkdown() {
    mockk<Editable> { assertThat(this.isBasedOnMarkdown).isFalse() }
  }

  @Test
  fun editableContainsSameTextAsTheMarkdownOnWhichItIsBased() {
    runTest {
      assertThat(
          buildMarkdown {
              bold { +':' }
              italic { +'p' }
            }
            .toEditableAsFlow(context)
            .single()
            .toString()
        )
        .isEqualTo(":p")
    }
  }

  @Test
  fun spansMarkdownBasedEditable() {
    runTest {
      assertThat(
          buildMarkdown {
              bold { +":)" }
              +' '
              italic { +":D" }
            }
            .toEditableAsFlow(context)
            .single()
            .getIndexedSpans(context)
        )
        .areStructurallyEqual(
          IndexedSpans(context, 0..2, StyleSpan(Typeface.BOLD)),
          IndexedSpans(context, 3..5, StyleSpan(Typeface.ITALIC))
        )
    }
  }
}

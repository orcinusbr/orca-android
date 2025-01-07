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

package br.com.orcinus.orca.composite.timeline.composition

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.toStringFun
import br.com.orcinus.orca.composite.timeline.composition.interop.CompositionTextFieldValue
import br.com.orcinus.orca.composite.timeline.composition.interop.spanned.IndexedSpans
import br.com.orcinus.orca.composite.timeline.composition.interop.spanned.getIndexedSpans
import br.com.orcinus.orca.composite.timeline.composition.interop.spanned.span.areStructurallyEqual
import br.com.orcinus.orca.composite.timeline.composition.interop.spanned.toMarkdown
import br.com.orcinus.orca.composite.timeline.test.composition.interop.scope.runCompositionTextFieldTest
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.buildMarkdown
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class CompositionTextFieldTests {
  @Test
  fun setsMarkdownAsText() {
    runCompositionTextFieldTest {
      val text = buildMarkdown {
        bold { +"Hello" }
        +", "
        italic { +"world" }
        +'!'
      }
      textField.setText(text)
      assertThat(textField.text).isNotNull().isInstanceOf<SpannableStringBuilder>().all {
        toStringFun().isEqualTo("Hello, world!")
        transform("indexed spans") { it.getIndexedSpans(context) }
          .areStructurallyEqual(
            IndexedSpans(context, 0..5, StyleSpan(Typeface.BOLD)),
            IndexedSpans(context, 7..12, StyleSpan(Typeface.ITALIC))
          )
      }
    }
  }

  @Test
  fun setsValue() {
    runCompositionTextFieldTest {
      textField.setValue(CompositionTextFieldValue(Markdown.unstyled(":P")))
      assertThat(textField).all {
        transform("text") { it.text?.toMarkdown(context) }.isEqualTo(Markdown.unstyled(":P"))
        transform("selection") { it.selectionStart..it.selectionEnd }.isEqualTo(2..2)
      }
    }
  }

  @Test
  fun listensToValueChange() {
    runCompositionTextFieldTest {
      lateinit var value: CompositionTextFieldValue
      textField.setOnValueChangeListener { value = it }
      textField.setValue(CompositionTextFieldValue(text = Markdown.unstyled("Hello, world!")))
      textField.setOnValueChangeListener(null)
      assertThat(value)
        .isEqualTo(CompositionTextFieldValue(text = Markdown.unstyled("Hello, world!")))
    }
  }

  @Test
  fun listensToValueChangeWhenTextIsSet() {
    runCompositionTextFieldTest {
      lateinit var value: CompositionTextFieldValue
      textField.setOnValueChangeListener { value = it }
      textField.setText("Hello, world!")
      textField.setOnValueChangeListener(null)
      assertThat(value)
        .isEqualTo(CompositionTextFieldValue(text = Markdown.unstyled("Hello, world!")))
    }
  }

  @Test
  fun listensToValueChangeWhenSelectionIsSet() {
    runCompositionTextFieldTest {
      lateinit var value: CompositionTextFieldValue
      textField.setText("Hello, world!")
      textField.setOnValueChangeListener { value = it }
      textField.setSelection(7, 12)
      textField.setOnValueChangeListener(null)
      assertThat(value)
        .isEqualTo(
          CompositionTextFieldValue(text = Markdown.unstyled("Hello, world!"), selection = 7..12)
        )
    }
  }

  @Test
  fun unsetsOnValueChangeListener() {
    runCompositionTextFieldTest {
      var value: CompositionTextFieldValue? = null
      textField.setOnValueChangeListener { value = it }
      textField.setOnValueChangeListener(null)
      textField.setValue(CompositionTextFieldValue(text = Markdown.unstyled("Hello, world!")))
      assertThat(value).isNull()
    }
  }
}

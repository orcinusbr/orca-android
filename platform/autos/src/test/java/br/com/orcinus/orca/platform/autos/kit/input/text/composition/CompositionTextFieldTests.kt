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

import android.app.Activity
import android.graphics.Typeface
import android.text.style.StyleSpan
import androidx.test.core.app.launchActivity
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.CompositionTextFieldValue
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.spanned.IndexedSpans
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.spanned.getIndexedSpans
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.spanned.span.areStructurallyEqual
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.spanned.toMarkdown
import br.com.orcinus.orca.platform.autos.test.kit.input.text.composition.interop.scope.runCompositionTextFieldTest
import br.com.orcinus.orca.platform.navigation.content
import br.com.orcinus.orca.platform.testing.activity.scenario.activity
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.buildMarkdown
import kotlin.test.Test
import kotlin.time.Duration
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class CompositionTextFieldTests {
  class WindowDetachmentActivity : Activity()

  @Test
  fun setsMarkdownText() {
    runCompositionTextFieldTest(
      @OptIn(ExperimentalCoroutinesApi::class) UnconfinedTestDispatcher()
    ) {
      textField.setText(
        buildMarkdown {
          italic { +"Hello" }
          +", "
          bold { +"world" }
          +'!'
        }
      )
      assertThat(textField.text).all {
        transform("text") { it?.toString() }.isEqualTo("Hello, world!")
        transform("indexed spans") { it?.getIndexedSpans(context) }
          .isNotNull()
          .areStructurallyEqual(
            IndexedSpans(context, 0..5, StyleSpan(Typeface.ITALIC)),
            IndexedSpans(context, 7..12, StyleSpan(Typeface.BOLD))
          )
      }
    }
  }

  @Test
  fun cancelsTextSettingsWhenSettingNonMarkdownBasedText() {
    runCompositionTextFieldTest {
      var exception: CancellationException? = null
      launch(Dispatchers.Unconfined) { delay(Duration.INFINITE) }
        .invokeOnCompletion { exception = it as? CancellationException }
      textField.setText("Hello, world!")
      assertThat(exception).isNotNull().isInstanceOf<CompositionTextField.ResetTextException>()
    }
  }

  @Test
  fun cancelsTextSettingsWhenDetachedFromTheWindow() {
    var exception: CancellationException? = null
    runTest {
      launch(Dispatchers.Unconfined) { delay(Duration.INFINITE) }
        .invokeOnCompletion { exception = it as? CancellationException }
      launchActivity<WindowDetachmentActivity>().use {
        it.activity?.content?.addView(CompositionTextField(context, this))
      }
    }
    assertThat(exception)
      .isNotNull()
      .isInstanceOf<CompositionTextField.DetachedFromWindowException>()
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

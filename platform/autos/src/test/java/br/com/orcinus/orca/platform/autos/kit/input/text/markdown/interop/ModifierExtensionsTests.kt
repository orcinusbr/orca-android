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

package br.com.orcinus.orca.platform.autos.kit.input.text.markdown.interop

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextInputSelection
import androidx.compose.ui.test.requestFocus
import androidx.compose.ui.text.TextRange
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import br.com.orcinus.orca.platform.autos.test.kit.input.text.markdown.interop.scope.onInteropEditText
import br.com.orcinus.orca.platform.autos.test.kit.input.text.markdown.interop.scope.runInteropEditTextTest
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ModifierExtensionsTests {
  @Test
  fun proxiesFocusRequestingFromViewToComposable() {
    runInteropEditTextTest {
      view.requestFocus()
      onInteropEditText().assertIsFocused()
    }
  }

  @Test
  fun proxiesSelectionSettingFromViewToComposable() {
    runInteropEditTextTest {
      view.setText("Hello, world!")
      view.setSelection(0, 5)
      assertThat(
          onInteropEditText()
            .fetchSemanticsNode()
            .config
            .getOrNull(SemanticsProperties.TextSelectionRange)
        )
        .isEqualTo(TextRange(0, 5))
    }
  }

  @Test
  fun proxiesTextSettingFromViewToComposable() {
    runInteropEditTextTest {
      view.setText("Hello, world!")
      onInteropEditText().assertTextEquals("Hello, world!")
    }
  }

  @Test
  fun proxiesFocusRequestingFromComposableToView() {
    runInteropEditTextTest {
      onInteropEditText().requestFocus()
      assertThat(view.isFocused).isTrue()
    }
  }

  @Test
  fun proxiesSelectionSettingFromComposableToView() {
    runInteropEditTextTest {
      onInteropEditText().performTextInput("Hello, world!")

      @OptIn(ExperimentalTestApi::class)
      onInteropEditText().performTextInputSelection(TextRange(0, 5))

      assertThat(view.selectionStart..view.selectionEnd).isEqualTo(0..5)
    }
  }

  @Test
  fun proxiesTextSettingFromComposableToView() {
    runInteropEditTextTest {
      onInteropEditText().performTextInput("Hello, world!")
      assertThat("${view.text}").isEqualTo("Hello, world!")
    }
  }
}

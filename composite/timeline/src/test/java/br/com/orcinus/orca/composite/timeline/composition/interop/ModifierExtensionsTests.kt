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

package br.com.orcinus.orca.composite.timeline.composition.interop

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
import br.com.orcinus.orca.composite.timeline.test.composition.interop.scope.onCompositionTextField
import br.com.orcinus.orca.composite.timeline.test.composition.interop.scope.runCompositionTextFieldTest
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ModifierExtensionsTests {
  @Test
  fun proxiesFocusRequestingFromViewToComposable() {
    runCompositionTextFieldTest {
      textField.requestFocus()
      onCompositionTextField().assertIsFocused()
    }
  }

  @Test
  fun proxiesSelectionSettingFromViewToComposable() {
    runCompositionTextFieldTest {
      textField.setText("Hello, world!")
      textField.setSelection(0, 5)
      assertThat(
          onCompositionTextField()
            .fetchSemanticsNode()
            .config
            .getOrNull(SemanticsProperties.TextSelectionRange)
        )
        .isEqualTo(TextRange(0, 5))
    }
  }

  @Test
  fun proxiesTextSettingFromViewToComposable() {
    runCompositionTextFieldTest {
      textField.setText("Hello, world!")
      onCompositionTextField().assertTextEquals("Hello, world!")
    }
  }

  @Test
  fun proxiesFocusRequestingFromComposableToView() {
    runCompositionTextFieldTest {
      onCompositionTextField().requestFocus()
      assertThat(textField.isFocused).isTrue()
    }
  }

  @Test
  fun proxiesSelectionSettingFromComposableToView() {
    runCompositionTextFieldTest {
      onCompositionTextField().performTextInput("Hello, world!")

      @OptIn(ExperimentalTestApi::class)
      onCompositionTextField().performTextInputSelection(TextRange(0, 5))

      assertThat(textField.selectionStart..textField.selectionEnd).isEqualTo(0..5)
    }
  }

  @Test
  fun proxiesTextSettingFromComposableToView() {
    runCompositionTextFieldTest {
      onCompositionTextField().performTextInput("Hello, world!")
      assertThat("${textField.text}").isEqualTo("Hello, world!")
    }
  }
}

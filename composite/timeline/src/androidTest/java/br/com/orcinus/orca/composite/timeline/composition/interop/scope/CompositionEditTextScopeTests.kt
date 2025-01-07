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

package br.com.orcinus.orca.composite.timeline.composition.interop.scope

import android.view.WindowInsets
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.composite.timeline.test.composition.interop.scope.runCompositionTextFieldTest
import kotlin.test.Test

internal class CompositionEditTextScopeTests {
  @Test
  fun runsImeAnimationTriggerOnce() {
    runCompositionTextFieldTest {
      var count = 0
      awaitImeAnimation {
        textField.windowInsetsController?.show(WindowInsets.Type.ime())
        count++
      }
      assertThat(count).isEqualTo(1)
    }
  }

  @Test
  fun awaitsImeOpeningAnimation() {
    runCompositionTextFieldTest {
      awaitImeAnimation { textField.windowInsetsController?.show(WindowInsets.Type.ime()) }
      assertThat(textField.rootWindowInsets?.isVisible(WindowInsets.Type.ime())).isEqualTo(true)
    }
  }

  @Test
  fun awaitsImeClosingAnimation() {
    runCompositionTextFieldTest {
      awaitImeAnimation { textField.windowInsetsController?.show(WindowInsets.Type.ime()) }
      awaitImeAnimation {
        UiThreadStatement.runOnUiThread {
          textField.windowInsetsController?.hide(WindowInsets.Type.ime())
        }
      }
      assertThat(textField.rootWindowInsets?.isVisible(WindowInsets.Type.ime())).isEqualTo(false)
    }
  }
}

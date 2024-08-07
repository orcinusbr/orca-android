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

import android.view.WindowInsets
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.platform.autos.test.kit.input.text.composition.interop.scope.runCompositionTextFieldTest
import kotlin.test.Test
import org.hamcrest.Matchers.`is`

internal class InteropEditTextTests {
  @Test
  fun opensImeWhenFocused() {
    runCompositionTextFieldTest {
      awaitImeAnimation { onView(`is`(textField)).perform(click()) }
      assertThat(textField.rootWindowInsets?.isVisible(WindowInsets.Type.ime())).isEqualTo(true)
    }
  }
}

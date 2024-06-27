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

package br.com.orcinus.orca.platform.autos.test.kit.input.text.markdown.interop.scope

import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.interop.InteropEditText
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class InteropEditTextScopeTests {
  @Test
  fun colorsAreProvidedOnce() {
    var count = 0
    runInteropEditTextTest({
      count++
      colors()
    }) {}
    assertThat(count).isEqualTo(1)
  }

  @Test
  fun bodyIsRunOnce() {
    var count = 0
    runInteropEditTextTest { count++ }
    assertThat(count).isEqualTo(1)
  }

  @Test
  fun viewIsColored() {
    lateinit var colors: TextFieldColors
    runInteropEditTextTest({
      colors = colors()
      colors
    }) {
      assertThat(view.colors).isEqualTo(colors)
    }
  }

  @Test
  fun viewContainsLeadingCompoundDrawable() {
    runInteropEditTextTest {
      assertThat(view.compoundDrawables[view.leadingCompoundDrawableIndex]).isNotNull()
    }
  }

  @Test
  fun viewContainsTrailingCompoundDrawable() {
    runInteropEditTextTest {
      assertThat(view.compoundDrawables[view.trailingCompoundDrawableIndex]).isNotNull()
    }
  }

  @Test
  fun compoundDrawablesAreUnsetAfterTest() {
    lateinit var view: InteropEditText
    runInteropEditTextTest { view = this.view }
    assertThat(view.compoundDrawables).containsExactly(null, null, null, null)
  }

  @Test
  fun addsContent() {
    runInteropEditTextTest {
      addContent { Text("🪐") }
      onNodeWithText("🪐").assertIsDisplayed()
    }
  }
}

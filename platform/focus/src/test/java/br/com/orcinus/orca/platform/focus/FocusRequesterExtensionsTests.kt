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

package br.com.orcinus.orca.platform.focus

import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.junit4.createComposeRule
import br.com.orcinus.orca.platform.autos.kit.input.text.FormTextField
import br.com.orcinus.orca.platform.autos.test.kit.input.text.onFormTextField
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class FocusRequesterExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun immediateFocusRequesterRequestsFocusImmediately() {
    composeRule
      .apply {
        setContent {
          AutosTheme { FormTextField(Modifier.focusRequester(rememberImmediateFocusRequester())) }
        }
      }
      .run {
        mainClock.advanceTimeBy(ImmediateFocusRequesterInitialDelay.inWholeMilliseconds)
        onFormTextField().assertIsFocused()
      }
  }
}

/*
 * Copyright © 2023–2024 Orcinus
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

package com.jeanbarrossilva.orca.platform.autos.kit.action.button.skeleton

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.skeleton.test.onLoadingIndicator
import org.junit.Rule
import org.junit.Test

internal class ButtonScopeTests {
  private val scope = ButtonScope()

  @get:Rule val composeRule = createComposeRule()

  @Test
  fun showsIndicatorWhenContentIsLoading() {
    with(composeRule) {
      setContent { scope.Loadable {} }
      scope.load { onLoadingIndicator().assertIsDisplayed() }
    }
  }

  @Test
  fun showsContentBeforeLoading() {
    with(composeRule) {
      setContent { scope.Loadable { Text("☀️") } }
      onNodeWithText("☀️").assertIsDisplayed()
      scope.load {}
    }
  }

  @Test
  fun showsContentAfterLoading() {
    with(composeRule) {
      setContent { scope.Loadable { Text("☀️") } }
      scope.load {}
      onNodeWithText("☀️").assertIsDisplayed()
    }
  }
}

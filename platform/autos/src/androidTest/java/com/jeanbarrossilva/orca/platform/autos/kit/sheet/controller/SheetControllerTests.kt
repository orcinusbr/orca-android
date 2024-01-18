/*
 * Copyright © 2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.autos.kit.sheet.controller

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.jeanbarrossilva.orca.platform.autos.kit.sheet.Sheet
import com.jeanbarrossilva.orca.platform.autos.kit.sheet.controller.test.SheetControllerTestRule
import com.jeanbarrossilva.orca.platform.autos.test.kit.sheet.onSheet
import com.jeanbarrossilva.orca.platform.autos.test.kit.sheet.setCurrentForTest
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class SheetControllerTests {
  @get:Rule val composeRule = createComposeRule()
  @get:Rule val controllerRule = SheetControllerTestRule()

  @Before
  fun setUp() {
    SheetController.setCurrentForTest()
  }

  @Test(expected = IllegalStateException::class)
  fun throwsWhenSettingCurrentControllerTwice() {
    SheetController.setCurrentForTest()
  }

  @Test
  fun getsCurrentController() {
    assertThat(SheetController.current).isNotNull()
  }

  @Test
  fun unsetsCurrentController() {
    SheetController.unsetCurrent()
    assertThat(SheetController.current).isNull()
  }

  @Test
  fun remembersAlwaysHiddenStateWhenCurrentControllerIsUnset() {
    SheetController.unsetCurrent()
    composeRule.setContent {
      @OptIn(ExperimentalMaterial3Api::class)
      SheetController.rememberCurrentOrHiddenState().let {
        DisposableEffect(it) {
          assertThat(it).isEqualTo(SheetState.AlwaysHidden)
          onDispose {}
        }
      }
    }
  }

  @Test
  fun remembersCurrentControllerStateWhenItIsSet() {
    composeRule.setContent {
      @OptIn(ExperimentalMaterial3Api::class)
      SheetController.rememberCurrentOrHiddenState().let {
        DisposableEffect(it) {
          assertThat(it).isEqualTo(SheetController.current?.state)
          onDispose {}
        }
      }
    }
  }

  @Test
  fun shows() {
    runTest { SheetController.current?.show {} }
    composeRule.apply { setContent { AutosTheme { Sheet() } } }.onSheet().assertIsDisplayed()
  }

  @Test
  fun showsContent() {
    runTest { SheetController.current?.show { content { Text("💒") } } }
    composeRule
      .apply { setContent { AutosTheme { Sheet() } } }
      .onNodeWithText("💒")
      .assertIsDisplayed()
  }

  @Test
  fun hides() {
    runTest {
      SheetController.current?.apply {
        show {}
        hide()
      }
    }
    composeRule.apply { setContent { AutosTheme { Sheet() } } }.onSheet().assertIsNotDisplayed()
  }
}

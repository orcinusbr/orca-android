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

package br.com.orcinus.orca.platform.autos.kit.input.text

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performTextInput
import br.com.orcinus.orca.platform.autos.test.kit.input.text.onSearchTextField
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SearchTextFieldTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun displaysLoadingIndicatorWhenLoading() {
    composeRule
      .apply { setContent { AutosTheme { SearchTextField(isLoading = true) } } }
      .onLoadingIndicator()
      .assertIsDisplayed()
  }

  @Test
  fun displaysSearchIconWhenLoaded() {
    composeRule
      .apply { setContent { AutosTheme { SearchTextField() } } }
      .onSearchIcon()
      .assertIsDisplayed()
  }

  @Test
  fun isTypedInto() {
    composeRule
      .apply {
        setContent {
          var query by remember { mutableStateOf("") }

          AutosTheme { SearchTextField(query = query, onQueryChange = { query = it }) }
        }
      }
      .onSearchTextField()
      .also { it.performTextInput("Hello, world!") }
      .assertTextEquals("Hello, world!")
  }
}

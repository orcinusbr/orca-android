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

package br.com.orcinus.orca.composite.timeline.search

import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import br.com.orcinus.orca.platform.autos.test.kit.input.text.onSearchTextField
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SearchableTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun mainContentIsShownByDefault() {
    composeRule
      .apply { setContent { AutosTheme { Searchable { content { Text("🫶🏽") } } } } }
      .onNodeWithText("🫶🏽")
      .assertIsDisplayed()
  }

  @Test
  fun replaceableContentIsShownByDefault() {
    composeRule
      .apply { setContent { AutosTheme { Searchable { content { Replaceable { Text("🧊") } } } } } }
      .onNodeWithText("🧊")
      .assertIsDisplayed()
  }

  @Test
  fun doesNotShowSearchTextFieldWhenReplaceableContentIsNotComposed() {
    composeRule
      .apply { setContent { AutosTheme { Searchable(content = SearchableScope::show) } } }
      .onSearchTextField()
      .assertDoesNotExist()
  }

  @Test
  fun showsSearchTextField() {
    composeRule
      .apply {
        setContent {
          AutosTheme {
            Searchable {
              content { Replaceable() }
              show()
            }
          }
        }
      }
      .onSearchTextField()
      .assertIsDisplayed()
  }

  @Test
  fun searchTextFieldIsTypedInto() {
    composeRule
      .apply {
        setContent {
          AutosTheme {
            Searchable {
              content {
                var query by remember { mutableStateOf("") }
                Replaceable(query = query, onQueryChange = { query = it })
              }
              show()
            }
          }
        }
      }
      .onSearchTextField()
      .also { it.performTextInput("Hello, world!") }
      .assertTextEquals("Hello, world!")
  }

  @Test
  fun dismissesSearchTextField() {
    composeRule
      .apply {
        setContent {
          AutosTheme {
            Searchable {
              content { Replaceable() }
              show()
              dismiss()
            }
          }
        }
      }
      .onSearchTextField()
      .assertDoesNotExist()
  }
}

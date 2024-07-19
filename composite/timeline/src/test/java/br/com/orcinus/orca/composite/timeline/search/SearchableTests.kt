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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.Dp
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
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
              content {
                Replaceable {
                  DisposableEffect(Unit) {
                    show()
                    onDispose {}
                  }
                }
              }
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

                Replaceable(query = query, onQueryChange = { query = it }) {
                  DisposableEffect(Unit) {
                    show()
                    onDispose {}
                  }
                }
              }
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

  @Test
  fun isNotSearchingByDefault() {
    composeRule.setContent { AutosTheme { Searchable { assertThat(isSearching).isFalse() } } }
  }

  @Test
  fun isNotSearchingWhenSearchTextFieldIsRequestedToBeShownButTheReplaceableContentIsNotComposed() {
    composeRule.setContent {
      AutosTheme {
        Searchable {
          show()
          assertThat(isSearching).isFalse()
        }
      }
    }
  }

  @Test
  fun isSearchingWhenSearchTextFieldIsShown() {
    composeRule.setContent {
      AutosTheme {
        Searchable {
          content {
            Replaceable {
              DisposableEffect(Unit) {
                show()
                assertThat(isSearching).isTrue()
                onDispose {}
              }
            }
          }
        }
      }
    }
  }

  @Test
  fun isNotSearchingWhenSearchTextFieldIsDismissed() {
    composeRule.setContent {
      AutosTheme {
        Searchable {
          content { Replaceable() }
          show()
          dismiss()
          assertThat(isSearching).isFalse()
        }
      }
    }
  }

  @Test
  fun doesNotUpdateBlurRadiusWhenReplaceableContentIsNotComposed() {
    composeRule.setContent {
      AutosTheme {
        Searchable {
          show()
          content {
            val blurRadius by contentBlurRadiusAsState

            DisposableEffect(Unit) {
              assertThat(blurRadius).isEqualTo(SearchableScope.MainContentBlurRadii.start)
              onDispose {}
            }
          }
        }
      }
    }
  }

  @Test
  fun updatesBlurRadiusWhenSearchTextFieldIsShown() {
    var contentBlurRadius = Dp.Unspecified
    composeRule.setContent {
      AutosTheme {
        Searchable {
          content {
            val blurRadius by contentBlurRadiusAsState

            DisposableEffect(blurRadius) {
              contentBlurRadius = blurRadius
              onDispose {}
            }

            Replaceable {
              DisposableEffect(Unit) {
                show()
                onDispose {}
              }
            }
          }
        }
      }
    }
    assertThat(contentBlurRadius).isEqualTo(SearchableScope.MainContentBlurRadii.endInclusive)
  }
}

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

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isGreaterThanOrEqualTo
import assertk.assertions.isTrue
import br.com.orcinus.orca.composite.timeline.search.content.SearchableContentScope
import br.com.orcinus.orca.composite.timeline.search.content.SearchableReplacementScope
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.core.sample.feed.profile.account.sample
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextFieldDefaults
import br.com.orcinus.orca.platform.autos.test.kit.input.text.onSearchTextField
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.core.sample
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.list.serializableListOf
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
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
      .apply { setContent { AutosTheme { Searchable { Text("🫶🏽") } } } }
      .onNodeWithText("🫶🏽")
      .assertIsDisplayed()
  }

  @Test(expected = IllegalStateException::class)
  fun throwsWhenMultipleReplaceablesAreComposed() {
    composeRule.setContent { AutosTheme { Searchable { repeat(2) { Replaceable() } } } }
  }

  @Test
  fun replaceableContentIsShownByDefault() {
    composeRule
      .apply { setContent { AutosTheme { Searchable { Replaceable { Text("🧊") } } } } }
      .onNodeWithText("🧊")
      .assertIsDisplayed()
  }

  @Test
  fun doesNotShowSearchTextFieldWhenReplaceableIsNotComposed() {
    composeRule
      .apply {
        setContent {
          AutosTheme {
            Searchable {
              var replacementScope by remember { mutableStateOf<SearchableReplacementScope?>(null) }
              var isReplaceableComposed by remember { mutableStateOf(true) }

              if (isReplaceableComposed) {
                Replaceable {
                  DisposableEffect(Unit) {
                    replacementScope = this@Replaceable
                    isReplaceableComposed = false
                    onDispose {}
                  }
                }
              }

              DisposableEffect(Unit) {
                replacementScope?.show()
                onDispose {}
              }
            }
          }
        }
      }
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
              Replaceable {
                DisposableEffect(Unit) {
                  show()
                  dismiss()
                  onDispose {}
                }
              }
            }
          }
        }
      }
      .onSearchTextField()
      .assertDoesNotExist()
  }

  @Test
  fun isNotSearchingByDefault() {
    composeRule.setContent {
      AutosTheme {
        Searchable {
          Replaceable {
            DisposableEffect(Unit) {
              assertThat(isSearching).isFalse()
              onDispose {}
            }
          }
        }
      }
    }
  }

  @Test
  fun isNotSearchingWhenSearchTextFieldIsRequestedToBeShownButTheReplaceableIsNotComposed() {
    composeRule.setContent {
      AutosTheme {
        Searchable {
          var replacementScope by remember { mutableStateOf<SearchableReplacementScope?>(null) }
          var isReplaceableComposed by remember { mutableStateOf(true) }

          if (isReplaceableComposed) {
            Replaceable {
              DisposableEffect(Unit) {
                replacementScope = this@Replaceable
                isReplaceableComposed = false
                onDispose {}
              }
            }
          }

          DisposableEffect(Unit) {
            onDispose {
              replacementScope?.show()
              assertThat(replacementScope?.isSearching).isEqualTo(false)
            }
          }
        }
      }
    }
  }

  @Test
  fun isSearchingWhenSearchTextFieldIsShown() {
    composeRule.setContent {
      AutosTheme {
        Searchable {
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

  @Test
  fun isNotSearchingWhenSearchTextFieldIsDismissed() {
    composeRule.setContent {
      AutosTheme {
        Searchable {
          Replaceable {
            DisposableEffect(Unit) {
              show()
              dismiss()
              assertThat(isSearching).isFalse()
              onDispose {}
            }
          }
        }
      }
    }
  }

  @Test
  fun doesNotUpdateBlurRadiusWhenReplaceableIsNotComposed() {
    composeRule.setContent {
      AutosTheme {
        Searchable {
          val blurRadius by contentBlurRadiusAsState

          DisposableEffect(Unit) {
            assertThat(blurRadius).isEqualTo(SearchableContentScope.BlurRadii.start)
            onDispose {}
          }
        }
      }
    }
  }

  @Test
  fun doesNotUpdateBlurRadiusWhenSearchTextFieldIsShownButNoSearchResultsAreFound() {
    var contentBlurRadius = Dp.Unspecified
    composeRule.setContent {
      AutosTheme {
        Searchable {
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
    assertThat(contentBlurRadius).isEqualTo(SearchableContentScope.BlurRadii.start)
  }

  @Test
  fun updatesBlurRadiusWhenSearchTextFieldIsShownAndSearchResultsAreFound() {
    var contentBlurRadius = Dp.Unspecified
    composeRule.setContent {
      AutosTheme {
        Searchable {
          val blurRadius by contentBlurRadiusAsState

          DisposableEffect(blurRadius) {
            contentBlurRadius = blurRadius
            onDispose {}
          }

          Replaceable(
            profileSearchResultsLoadable =
              ListLoadable.Populated(serializableListOf(ProfileSearchResult.sample))
          ) {
            DisposableEffect(Unit) {
              show()
              onDispose {}
            }
          }
        }
      }
    }
    assertThat(contentBlurRadius).isEqualTo(SearchableContentScope.BlurRadii.endInclusive)
  }

  @Test
  fun zeroesBlurRadiusWhenSearchTextFieldIsShownButSearchResultsAreFoundAndThenAreNot() {
    var contentBlurRadius = Dp.Unspecified
    composeRule
      .apply {
        setContent {
          AutosTheme {
            Searchable {
              val query by remember { mutableStateOf("") }
              val profileSearchResultsLoadable by
                remember(query) {
                  derivedStateOf {
                    ProfileSearchResult.sample
                      .takeIf { query.startsWith("${it.account}") }
                      .let(::listOfNotNull)
                      .toSerializableList()
                      .toListLoadable()
                  }
                }
              val blurRadius by contentBlurRadiusAsState

              DisposableEffect(blurRadius) {
                contentBlurRadius = blurRadius
                onDispose {}
              }

              Replaceable(profileSearchResultsLoadable = profileSearchResultsLoadable) {
                DisposableEffect(Unit) {
                  show()
                  onDispose {}
                }
              }
            }
          }
        }
      }
      .onSearchTextField()
      .performTextInput("${Account.sample}".take(1) + "不定")
    assertThat(contentBlurRadius).isEqualTo(SearchableContentScope.BlurRadii.start)
  }

  @Test
  fun searchTextFieldLayoutHeightIsZeroedByDefault() {
    composeRule.setContent {
      AutosTheme {
        Searchable {
          Replaceable()

          DisposableEffect(Unit) {
            assertThat(searchTextFieldLayoutHeight).isEqualTo(0.dp)
            onDispose {}
          }
        }
      }
    }
  }

  @Test
  fun zeroesSearchTextFieldLayoutHeightWhenSearchTextFieldIsDismissed() {
    composeRule.setContent {
      AutosTheme {
        Searchable {
          Replaceable {
            DisposableEffect(Unit) {
              show()
              dismiss()
              onDispose {}
            }
          }

          DisposableEffect(Unit) {
            onDispose { assertThat(searchTextFieldLayoutHeight).isEqualTo(0.dp) }
          }
        }
      }
    }
  }

  @Test
  fun updatesSearchTextFieldLayoutHeightWhenSearchTextFieldIsShown() {
    composeRule.setContent {
      AutosTheme {
        Searchable {
          val density = LocalDensity.current
          val textStyle = LocalTextStyle.current
          val fontSizeInDp =
            remember(density, textStyle) { with(density) { textStyle.fontSize.toDp() } }
          val searchTextFieldSpacing = SearchTextFieldDefaults.spacing

          Replaceable {
            DisposableEffect(Unit) {
              show()
              onDispose {}
            }
          }

          DisposableEffect(Unit) {
            onDispose {
              assertThat(searchTextFieldLayoutHeight)
                .isGreaterThanOrEqualTo(fontSizeInDp + searchTextFieldSpacing * 4)
            }
          }
        }
      }
    }
  }
}

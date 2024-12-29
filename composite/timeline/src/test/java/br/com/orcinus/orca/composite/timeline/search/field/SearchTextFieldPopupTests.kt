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

package br.com.orcinus.orca.composite.timeline.search.field

import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso.pressBack
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import assertk.assertions.prop
import br.com.orcinus.orca.composite.timeline.test.search.field.onDismissButton
import br.com.orcinus.orca.platform.autos.test.kit.input.text.onSearchTextField
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.loadable.placeholder.test.assertIsLoading
import com.jeanbarrossilva.loadable.placeholder.test.assertIsNotLoading
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SearchTextFieldPopupTests {
  @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

  @Test
  fun shows() {
    composeRule.activity.setContent { AutosTheme { SearchTextFieldPopup() } }
    composeRule.onSearchTextField().assertIsDisplayed()
  }

  @Test
  fun recomposesWhenQueryChanges() {
    composeRule.setContent {
      var query by remember { mutableStateOf("") }
      AutosTheme { SearchTextFieldPopup(query = query, onQueryChange = { query = it }) }
    }
    composeRule.onSearchTextField().apply { performTextInput("❤️‍🩹") }.assertTextEquals("❤️‍🩹")
  }

  @Test
  fun isLoadingWhenResultsAreLoading() {
    composeRule
      .apply {
        setContent { AutosTheme { SearchTextFieldPopup(resultsLoadable = ListLoadable.Loading()) } }
      }
      .onSearchTextField()
      .assertIsLoading()
  }

  @Test
  fun isLoadedWhenResultsAreLoaded() {
    composeRule
      .apply { setContent { AutosTheme { SearchTextFieldPopup() } } }
      .onSearchTextField()
      .assertIsNotLoading()
  }

  @Test
  fun dismisses() {
    composeRule.setContent { AutosTheme { SearchTextFieldPopup() } }
    composeRule.activity.setContent {}
    composeRule.onSearchTextField().assertDoesNotExist()
  }

  @Test
  fun dismissesWhenBackPressing() {
    composeRule.setContent { AutosTheme { SearchTextFieldPopup() } }
    pressBack()
    composeRule.onSearchTextField().assertDoesNotExist()
  }

  @Test
  fun dismissesWhenClickingDismissalButton() {
    composeRule.setContent { AutosTheme { SearchTextFieldPopup() } }
    composeRule.onDismissButton().performClick()
    composeRule.onSearchTextField().assertDoesNotExist()
  }

  @Test
  fun deconfiguresViewTreeOwnershipWhenDismissed() {
    val originalOwnedTreeView = View(composeRule.activity)
    composeRule.activity.setContentView(originalOwnedTreeView)
    val originalViewTreeOwner = ViewTreeOwner.of(originalOwnedTreeView)
    composeRule.activity.setContent { AutosTheme { SearchTextFieldPopup() } }
    composeRule.onDismissButton().performClick()
    assertThat(ViewTreeOwner)
      .prop("from") { composeRule.activity.window?.decorView?.let(it::of) }
      .isEqualTo(originalViewTreeOwner)
  }

  @Test
  fun listensToDismissal() {
    var didDismiss = false
    composeRule.activity.setContent {
      AutosTheme { SearchTextFieldPopup(onDidDismiss = { didDismiss = true }) }
    }
    composeRule.onDismissButton().performClick()
    composeRule.waitForIdle()
    assertThat(didDismiss, "didDismiss").isTrue()
  }
}

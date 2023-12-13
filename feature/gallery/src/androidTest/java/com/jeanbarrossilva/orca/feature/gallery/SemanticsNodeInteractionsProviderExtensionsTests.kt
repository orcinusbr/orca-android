/*
 * Copyright © 2023 Orca
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

package com.jeanbarrossilva.orca.feature.gallery

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import com.jeanbarrossilva.orca.feature.gallery.test.onActions
import com.jeanbarrossilva.orca.feature.gallery.test.onCloseActionButton
import com.jeanbarrossilva.orca.feature.gallery.test.onDownloadItem
import com.jeanbarrossilva.orca.feature.gallery.test.onOptionsButton
import com.jeanbarrossilva.orca.feature.gallery.test.onOptionsMenu
import com.jeanbarrossilva.orca.feature.gallery.test.onPager
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import org.junit.Rule
import org.junit.Test

internal class SemanticsNodeInteractionsProviderExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun findsActions() {
    composeRule.apply { setContent { AutosTheme { Gallery() } } }.onActions().assertIsDisplayed()
  }

  @Test
  fun findsCloseActionButton() {
    composeRule
      .apply { setContent { AutosTheme { Actions() } } }
      .onCloseActionButton()
      .assertIsDisplayed()
  }

  @Test
  fun findsOptionsButton() {
    composeRule
      .apply { setContent { AutosTheme { Actions() } } }
      .onOptionsButton()
      .assertIsDisplayed()
  }

  @Test
  fun findsOptionsMenu() {
    composeRule
      .apply { setContent { AutosTheme { Gallery() } } }
      .apply { onOptionsButton().performClick() }
      .onOptionsMenu()
      .assertIsDisplayed()
  }

  @Test
  fun findsDownloadItem() {
    composeRule
      .apply { setContent { AutosTheme { Actions(areOptionsVisible = true) } } }
      .onDownloadItem()
      .assertIsDisplayed()
  }

  @Test
  fun findsPager() {
    composeRule.apply { setContent { AutosTheme { Gallery() } } }.onPager().assertIsDisplayed()
  }
}

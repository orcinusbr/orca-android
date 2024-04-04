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

package br.com.orcinus.orca.feature.gallery.test.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import br.com.orcinus.orca.feature.gallery.test.ui.page.onPage
import br.com.orcinus.orca.feature.gallery.ui.Actions
import br.com.orcinus.orca.feature.gallery.ui.Gallery
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SemanticsNodeInteractionsProviderExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun findsCloseActionButton() {
    composeRule
      .apply { setContent { AutosTheme { Actions() } } }
      .onCloseActionButton()
      .assertIsDisplayed()
  }

  @Test
  fun findsPager() {
    composeRule.apply { setContent { AutosTheme { Gallery() } } }.onPager().assertIsDisplayed()
  }

  @Test
  fun findsPage() {
    composeRule.apply { setContent { AutosTheme { Gallery() } } }.onPage().assertIsDisplayed()
  }
}

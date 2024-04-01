/*
 * Copyright © 2023-2024 Orcinus
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

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import br.com.orcinus.orca.feature.gallery.test.ui.page.isPage
import br.com.orcinus.orca.feature.gallery.ui.Gallery
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import org.junit.Rule
import org.junit.Test

internal class SemanticsMatcherExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun matchesImage() {
    composeRule
      .apply {
        setContent {
          Image(
            painterResource(br.com.orcinus.orca.std.image.compose.R.drawable.image),
            contentDescription = "Image"
          )
        }
      }
      .onNode(isImage())
      .assertIsDisplayed()
  }

  @Test
  fun matchesPager() {
    composeRule
      .apply { setContent { AutosTheme { Gallery() } } }
      .onNode(isPager())
      .assertIsDisplayed()
  }

  @Test
  fun matchesPage() {
    composeRule
      .apply { setContent { AutosTheme { Gallery() } } }
      .onPager()
      .onChildren()
      .filterToOne(isPage())
      .assertIsDisplayed()
  }
}

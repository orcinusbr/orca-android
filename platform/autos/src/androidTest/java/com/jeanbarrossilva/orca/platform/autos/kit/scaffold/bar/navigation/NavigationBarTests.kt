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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.navigation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import assertk.assertThat
import assertk.assertions.isTrue
import com.jeanbarrossilva.orca.platform.autos.test.kit.scaffold.bar.navigation.onTab
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import kotlin.test.Test
import org.junit.Rule

internal class NavigationBarTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun notifiesOfClickOnTab() {
    var hasTabBeenClicked = false
    composeRule
      .apply {
        setContent {
          AutosTheme {
            NavigationBar(title = {}, action = {}) {
              tab(onClick = { hasTabBeenClicked = true }) {}
            }
          }
        }
      }
      .onTab()
      .performClick()
    assertThat(hasTabBeenClicked).isTrue()
  }
}

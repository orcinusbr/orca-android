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

package br.com.orcinus.orca.composite.timeline.stat.activateable

import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import br.com.orcinus.orca.composite.timeline.stat.activateable.test.onActivateableStatIcon
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

internal class ActivateableStatIconTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun isUnselectedWhenInactive() {
    composeRule.setContent { AutosTheme { ActivateableStatIcon(isActive = false) } }
    composeRule.onActivateableStatIcon().assertIsNotSelected()
  }

  @Test
  fun isSelectedWhenActive() {
    composeRule.setContent { AutosTheme { ActivateableStatIcon(isActive = true) } }
    composeRule.onActivateableStatIcon().assertIsSelected()
  }

  @Test
  fun receivesInteractionWhenInteractive() {
    var hasBeenInteractedWith = false
    composeRule.setContent {
      AutosTheme {
        ActivateableStatIcon(
          interactiveness =
            ActivateableStatIconInteractiveness.Interactive { hasBeenInteractedWith = true }
        )
      }
    }
    composeRule.onActivateableStatIcon().performClick()
    assertTrue(hasBeenInteractedWith)
  }
}

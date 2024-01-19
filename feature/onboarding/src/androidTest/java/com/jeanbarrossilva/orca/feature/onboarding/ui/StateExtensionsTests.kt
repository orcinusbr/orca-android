/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.feature.onboarding.ui

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

internal class StateExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun animatesStringAsState() {
    composeRule.setContent { Text(animateStringAsState("Hello, world!").value) }
    composeRule.mainClock.advanceTimeBy(
      milliseconds =
        (0..12).sumOf { calculateNextCharDelayInMillisecondsForAnimatedAsStateString(it, 12) }
    )
    composeRule.onNodeWithText("Hello, world!").assertIsDisplayed()
  }
}

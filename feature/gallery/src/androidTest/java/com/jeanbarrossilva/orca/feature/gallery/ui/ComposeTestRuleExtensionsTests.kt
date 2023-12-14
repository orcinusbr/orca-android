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

package com.jeanbarrossilva.orca.feature.gallery.ui

import android.view.ViewConfiguration
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import assertk.assertThat
import assertk.assertions.isGreaterThanOrEqualTo
import com.jeanbarrossilva.orca.feature.gallery.ui.test.onPage
import com.jeanbarrossilva.orca.feature.gallery.ui.test.waitForDoubleTapTimeout
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import org.junit.Rule
import org.junit.Test

internal class ComposeTestRuleExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun findsCurrentPage() {
    composeRule.apply { setContent { AutosTheme { Gallery() } } }.onPage().assertIsDisplayed()
  }

  @Test
  fun waitsForDoubleTapTimeout() {
    assertThat(
        composeRule.apply(ComposeContentTestRule::waitForDoubleTapTimeout).mainClock.currentTime
      )
      .isGreaterThanOrEqualTo(ViewConfiguration.getDoubleTapTimeout().toLong())
  }
}

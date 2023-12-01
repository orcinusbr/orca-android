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

package com.jeanbarrossilva.orca.platform.ui.component.timeline.post.headline

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.headline.onHeadlineCard
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

internal class HeadlineCardTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun runsCallbackWhenClicked() {
    var hasCallbackBeenRun = false
    composeRule.setContent { AutosTheme { HeadlineCard(onClick = { hasCallbackBeenRun = true }) } }
    composeRule.onHeadlineCard().performClick()
    assertTrue(hasCallbackBeenRun)
  }
}

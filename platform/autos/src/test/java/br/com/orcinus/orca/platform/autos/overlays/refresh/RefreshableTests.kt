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

package br.com.orcinus.orca.platform.autos.overlays.refresh

import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import br.com.orcinus.orca.platform.autos.test.overlays.refresh.onRefreshIndicator
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class RefreshableTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun showsRefreshIndicatorWhenSwipingDown() {
    composeRule
      .apply { setContent { Refreshable(Refresh.immediate(Refresh.Listener.Empty)) {} } }
      .run {
        onRoot().performTouchInput(TouchInjectionScope::swipeDown)
        onRefreshIndicator().assertIsDisplayed()
      }
  }
}

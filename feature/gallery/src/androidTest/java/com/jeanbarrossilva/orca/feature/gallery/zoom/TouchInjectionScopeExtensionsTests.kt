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

package com.jeanbarrossilva.orca.feature.gallery.zoom

import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performTouchInput
import com.jeanbarrossilva.orca.feature.gallery.Gallery
import com.jeanbarrossilva.orca.feature.gallery.test.onPage
import com.jeanbarrossilva.orca.feature.gallery.test.zoom.assertIsZoomedIn
import com.jeanbarrossilva.orca.feature.gallery.test.zoom.assertIsZoomedOut
import com.jeanbarrossilva.orca.feature.gallery.test.zoom.performZoomIn
import com.jeanbarrossilva.orca.feature.gallery.test.zoom.zoomIn
import com.jeanbarrossilva.orca.feature.gallery.test.zoom.zoomOut
import org.junit.Rule
import org.junit.Test

internal class TouchInjectionScopeExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun zoomsIn() {
    composeRule
      .apply { setContent { Gallery() } }
      .onPage()
      .performTouchInput(TouchInjectionScope::zoomIn)
      .assertIsZoomedIn()
  }

  @Test
  fun zoomsOut() {
    composeRule
      .apply { setContent { Gallery() } }
      .onPage()
      .performZoomIn()
      .performTouchInput(TouchInjectionScope::zoomOut)
      .assertIsZoomedOut()
  }
}

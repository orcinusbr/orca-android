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

package com.jeanbarrossilva.orca.feature.gallery.ui.test

import android.view.ViewConfiguration
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildren
import com.jeanbarrossilva.loadable.placeholder.test.isNotLoading
import com.jeanbarrossilva.orca.feature.gallery.ui.Gallery
import kotlin.time.Duration.Companion.seconds

/**
 * [SemanticsNodeInteraction] of a [Gallery]'s [HorizontalPager]'s current page.
 *
 * @see onPager
 */
internal fun ComposeTestRule.onPage(): SemanticsNodeInteraction {
  return onPager().onChildren().filterToOne(isPage()).also {
    waitUntil(4.seconds.inWholeMilliseconds) { it[isNotLoading()] }
  }
}

/**
 * Waits until the double tap timeout duration (as it is defined by [ViewConfiguration]) has been
 * elapsed.
 *
 * @see ViewConfiguration.getDoubleTapTimeout
 */
internal fun ComposeTestRule.waitForDoubleTapTimeout() {
  val timeout = ViewConfiguration.getDoubleTapTimeout().toLong()
  mainClock.advanceTimeBy(timeout)
}

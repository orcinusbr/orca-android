/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.orca.composite.timeline.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.composite.timeline.TIMELINE_REFRESH_INDICATOR
import com.jeanbarrossilva.orca.composite.timeline.Timeline

/** [SemanticsNodeInteraction] of a [Timeline]'s refresh indicator. */
fun ComposeTestRule.onRefreshIndicator(): SemanticsNodeInteraction {
  return onNodeWithTag(TIMELINE_REFRESH_INDICATOR)
}

/** [SemanticsNodeInteraction] of a [Timeline]. */
fun ComposeTestRule.onTimeline(): SemanticsNodeInteraction {
  return onNode(isTimeline())
}

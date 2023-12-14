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

package com.jeanbarrossilva.orca.platform.ui.test.component.stat.favorite

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.platform.ui.component.stat.Stats
import com.jeanbarrossilva.orca.platform.ui.component.stat.favorite.FAVORITE_STAT_TAG

/** [SemanticsNodeInteraction] of [Stats]' favorite stat count. */
fun ComposeTestRule.onFavoriteStat(): SemanticsNodeInteraction {
  return onNodeWithTag(FAVORITE_STAT_TAG, useUnmergedTree = true)
}

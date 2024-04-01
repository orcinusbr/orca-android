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

package br.com.orcinus.orca.composite.timeline.test.stat

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.onNodeWithTag
import br.com.orcinus.orca.composite.timeline.stat.STATS_COMMENT_STAT_TAG
import br.com.orcinus.orca.composite.timeline.stat.STATS_SHARE_STAT_TAG
import br.com.orcinus.orca.composite.timeline.stat.Stats

/** [SemanticsNodeInteraction] of [Stats]' comment stat. */
fun SemanticsNodeInteractionsProvider.onCommentStat(): SemanticsNodeInteraction {
  return onNodeWithTag(STATS_COMMENT_STAT_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [Stats]' share stat. */
fun SemanticsNodeInteractionsProvider.onShareStat(): SemanticsNodeInteraction {
  return onNodeWithTag(STATS_SHARE_STAT_TAG, useUnmergedTree = true)
}

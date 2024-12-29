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

package br.com.orcinus.orca.composite.timeline.test.search.field

import androidx.compose.material3.Card
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import br.com.orcinus.orca.composite.timeline.search.field.ResultSearchTextField

/**
 * [SemanticsNodeInteraction] of a [ResultSearchTextField]'s "dismiss" button.
 *
 * @see isDismissButton
 */
fun SemanticsNodeInteractionsProvider.onDismissButton(): SemanticsNodeInteraction {
  return onNode(isDismissButton(), useUnmergedTree = true)
}

/**
 * [SemanticsNodeInteraction] of a [ResultSearchTextField]'s result [Card].
 *
 * @see onResultCards
 * @see isResultCard
 */
fun SemanticsNodeInteractionsProvider.onResultCard(): SemanticsNodeInteraction {
  return onNode(isResultCard())
}

/**
 * [SemanticsNodeInteraction] of a [ResultSearchTextField]'s result [Card]s.
 *
 * @see onResultCard
 * @see isResultCard
 */
fun SemanticsNodeInteractionsProvider.onResultCards(): SemanticsNodeInteractionCollection {
  return onAllNodes(isResultCard())
}

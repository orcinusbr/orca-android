/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.orca.composite.timeline.test.post.figure.link

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.composite.timeline.post.figure.link.LINK_CARD_TAG
import com.jeanbarrossilva.orca.composite.timeline.post.figure.link.LinkCard

/** [SemanticsNodeInteraction] of a [LinkCard]. */
fun ComposeTestRule.onLinkCard(): SemanticsNodeInteraction {
  return onNodeWithTag(LINK_CARD_TAG)
}

/** [SemanticsNodeInteractionCollection] of [LinkCard]s. */
fun ComposeTestRule.onLinkCards(): SemanticsNodeInteractionCollection {
  return onAllNodesWithTag(LINK_CARD_TAG)
}

package com.jeanbarrossilva.mastodonte.platform.ui.test.component.timeline.toot

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert

/**
 * Asserts that the node is marked as favorite.
 *
 * @see isFavorite
 **/
fun SemanticsNodeInteraction.assertIsFavorite(): SemanticsNodeInteraction {
    return assert(isFavorite())
}

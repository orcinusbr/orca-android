package com.jeanbarrossilva.mastodonte.platform.ui.component

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag

/** [SemanticsNodeInteraction] of a [FavoriteIcon]. **/
internal fun ComposeTestRule.onFavoriteIcon(): SemanticsNodeInteraction {
    return onNodeWithTag(FAVORITE_ICON_TAG)
}

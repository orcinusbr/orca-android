package com.jeanbarrossilva.mastodonte.platform.ui.test.component.timeline.toot

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.Favorite

/** [SemanticsMatcher] that indicates whether the node is marked as favorite. **/
fun isFavorite(): SemanticsMatcher {
    return SemanticsMatcher.expectValue(SemanticsProperties.Favorite, true)
}

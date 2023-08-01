package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.onChildAt

/** [SemanticsNodeInteraction] of this [Stat]'s label node. **/
internal fun SemanticsNodeInteraction.onStatLabel(): SemanticsNodeInteraction {
    return onChildAt(1)
}

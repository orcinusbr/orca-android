package com.jeanbarrossilva.orca.feature.feed.test

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsSelected

/** Whether the [SemanticsNode] is selected. **/
internal val SemanticsNodeInteraction.isSelected
    get() = try {
        assertIsSelected()
        true
    } catch (_: AssertionError) {
        false
    }

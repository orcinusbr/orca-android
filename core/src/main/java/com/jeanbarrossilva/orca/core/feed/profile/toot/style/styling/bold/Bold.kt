package com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.bold

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.Style

/** [Style] that applies a heavier weight to the target's font. **/
data class Bold(override val indices: IntRange) : Style() {
    companion object {
        /** [Char] that indicates the start and the end of a bold target. **/
        internal const val SYMBOL = '*'

        /** Escaped [Regex]-specific version of [SYMBOL]. **/
        internal const val ESCAPED_SYMBOL = "\\*"
    }
}

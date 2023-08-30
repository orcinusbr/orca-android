package com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.Style
import java.net.URL

/**
 * [Style] for referencing to a username.
 *
 * @param indices Indices at which both the style symbols and the target are in the whole [String].
 * @param url [URL] that leads to the owner of the mentioned username.
 **/
data class Mention(override val indices: IntRange, val url: URL) : Style() {
    companion object {
        /** [Char] that indicates the start of a [Mention] by default. **/
        internal const val SYMBOL = '@'
    }
}

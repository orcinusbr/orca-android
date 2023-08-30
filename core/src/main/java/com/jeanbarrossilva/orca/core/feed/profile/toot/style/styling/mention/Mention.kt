package com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention

import java.io.Serializable
import java.net.URL

/**
 * Reference to a username.
 *
 * @param indices [String]'s [indices][String.indices].
 * @param url [URL] that leads to the owner of the mentioned username.
 **/
data class Mention(val indices: IntRange, val url: URL) : Serializable {
    companion object {
        /** [Char] that indicates the start of a [Mention] by default. **/
        internal const val SYMBOL = '@'
    }
}

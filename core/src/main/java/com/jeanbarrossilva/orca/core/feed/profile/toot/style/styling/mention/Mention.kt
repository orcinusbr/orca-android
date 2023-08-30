package com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.Style
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.Style.Delimiter
import java.net.URL

/**
 * [Style] for referencing to a username.
 *
 * @param _delimiter [Delimiter] by which this [Style] is delimited.
 * @param indices Indices at which both the style symbols and the target are in the whole [String].
 * @param url [URL] that leads to the owner of the mentioned username.
 **/
data class Mention(
    @Suppress("PropertyName") val _delimiter: Delimiter,
    override val indices: IntRange,
    val url: URL
) : Style() {
    constructor(@Suppress("LocalVariableName") _indices: IntRange, url: URL) :
        this(SymbolMentionDelimiter, _indices, url)

    override fun getDelimiter(): Delimiter {
        return _delimiter
    }

    companion object {
        /** [Char] that indicates the start of a [Mention] by default. **/
        internal const val SYMBOL = '@'
    }
}

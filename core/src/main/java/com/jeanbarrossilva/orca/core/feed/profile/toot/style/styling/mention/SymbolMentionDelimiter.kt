package com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.Style

/**
 * [Style.Delimiter] that considers [Mention]s parts of a [String] starting with a [Mention.SYMBOL]
 * and determines its end by the absence of alphanumeric or some other special characters.
 **/
object SymbolMentionDelimiter : Style.Delimiter() {
    override fun getRegex(): Regex {
        return Regex("${Mention.SYMBOL}[a-zA-Z0-9._%+-]+")
    }

    override fun onGetTarget(match: String): String {
        return match.removePrefix("${Mention.SYMBOL}")
    }
}

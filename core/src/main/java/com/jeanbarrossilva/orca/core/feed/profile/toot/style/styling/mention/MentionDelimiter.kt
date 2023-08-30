package com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.Style

/** [Style.Delimiter] for [Mention]s. **/
abstract class MentionDelimiter : Style.Delimiter() {
    final override val parent = symbol

    companion object {
        /**
         * [Style.Delimiter] that considers [Mention]s parts of a [String] starting with a [Mention.SYMBOL]
         * and determines its end by the absence of alphanumeric or some other special characters.
         **/
        internal val symbol = object : Style.Delimiter() {
            override val parent = null
            override val regex = Regex("${Mention.SYMBOL}[a-zA-Z0-9._%+-]+")

            override fun onGetTarget(match: String): String {
                return match.removePrefix("${Mention.SYMBOL}")
            }

            override fun onTarget(target: String): String {
                return Mention.SYMBOL + target
            }
        }
    }
}

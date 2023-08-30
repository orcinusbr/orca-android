package com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.bold

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.Style

/** [Style.Delimiter] for emboldened portions of a [String]. **/
abstract class BoldDelimiter : Style.Delimiter() {
    final override val parent = symbol

    companion object {
        /**
         * [BoldDelimiter] that considers [Bold] parts of a [String] surrounded by a [Bold.SYMBOL].
         **/
        internal val symbol = object : Style.Delimiter() {
            override val parent = null
            override val regex = Regex("${Bold.ESCAPED_SYMBOL}(.*?)${Bold.ESCAPED_SYMBOL}")

            override fun onGetTarget(match: String): String {
                return match.substringAfter(Bold.SYMBOL).substringBefore(Bold.SYMBOL)
            }

            override fun onTarget(target: String): String {
                return Bold.SYMBOL + target + Bold.SYMBOL
            }
        }
    }
}

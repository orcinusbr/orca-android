package com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.bold

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.Style

/** [Style.Delimiter] for emboldened portions of a [String]. **/
sealed class BoldDelimiter : Style.Delimiter() {
    /**
     * [BoldDelimiter] that's the [parent] of all [BoldDelimiter.Child] instances and considers
     * [Bold] parts of a [String] surrounded by a [Bold.SYMBOL].
     **/
    class Parent private constructor() : BoldDelimiter() {
        override val parent = null
        override val regex = Regex("${Bold.ESCAPED_SYMBOL}(.*?)${Bold.ESCAPED_SYMBOL}")

        override fun onGetTarget(match: String): String {
            return match.substringAfter(Bold.SYMBOL).substringBefore(Bold.SYMBOL)
        }

        override fun onTarget(target: String): String {
            return Bold.SYMBOL + target + Bold.SYMBOL
        }

        companion object {
            /** Single instance of a [Parent]. **/
            internal val instance = Parent()
        }
    }

    /** [BoldDelimiter] that's a child of [Parent]. **/
    abstract class Child : BoldDelimiter() {
        final override val parent = Parent.instance
    }
}

package com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.Style

/** [Style.Delimiter] for [Mention]s. **/
sealed class MentionDelimiter : Style.Delimiter() {
    /**
     * [MentionDelimiter] that's the [parent] of all [Child] instances and considers [Mention]s
     * parts of a [String] starting with a [Mention.SYMBOL] and determines its end by the absence of
     * alphanumeric or some other special characters.
     **/
    class Parent private constructor() : MentionDelimiter() {
        override val parent = null
        override val regex = Regex("${Mention.SYMBOL}[a-zA-Z0-9._%+-]+")

        override fun onGetTarget(match: String): String {
            return match.removePrefix("${Mention.SYMBOL}")
        }

        override fun onTarget(target: String): String {
            return Mention.SYMBOL + target
        }

        companion object {
            /** Single instance of a [Parent]. **/
            internal val instance = Parent()
        }
    }

    /** [MentionDelimiter] that's a child of [Parent]. **/
    abstract class Child : MentionDelimiter() {
        final override val parent = Parent.instance
    }
}

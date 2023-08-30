package com.jeanbarrossilva.orca.core.feed.profile.toot.style.type

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.Style

/** [Style] for a specific subject. **/
data class Hashtag(override val indices: IntRange) : Style() {
    /** [Style.Delimiter] for [Mention]s. **/
    sealed class Delimiter : Style.Delimiter() {
        /**
         * [Delimiter] that's the [parent] of all [Child] instances and considers [Hashtag]s parts
         * of a [String] starting with a [SYMBOL] and determines its end by the absence of
         * alphanumeric or some other special characters.
         **/
        class Parent private constructor() : Delimiter() {
            override val parent = null
            override val regex = Hashtag.regex

            override fun onGetTarget(match: String): String {
                return match.removePrefix("$SYMBOL")
            }

            override fun onTarget(target: String): String {
                return SYMBOL + target
            }

            companion object {
                /** Single instance of a [Parent]. **/
                internal val instance = Parent()
            }
        }

        /** [Delimiter] that's a child of [Parent]. **/
        abstract class Child : Delimiter() {
            final override val parent = Parent.instance
        }
    }

    companion object {
        /** [Char] that indicates the start of a [Hashtag] by default. **/
        const val SYMBOL = '#'

        /** [Regex] that matches a [Hashtag]'s target. **/
        val targetRegex = Regex("\\w+")

        /** [Regex] that matches a [Hashtag]. **/
        internal val regex = Regex("#$targetRegex")
    }
}

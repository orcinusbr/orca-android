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
    /** [Style.Delimiter] for [Mention]s. **/
    sealed class Delimiter : Style.Delimiter() {
        /**
         * [Delimiter] that's the [parent] of all [Child] instances and considers [Mention]s
         * parts of a [String] starting with a [Mention.SYMBOL] and determines its end by the absence of
         * alphanumeric or some other special characters.
         **/
        class Parent private constructor() : Delimiter() {
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

        /** [Delimiter] that's a child of [Parent]. **/
        abstract class Child : Delimiter() {
            final override val parent = Parent.instance
        }
    }

    companion object {
        /** [Char] that indicates the start of a [Mention] by default. **/
        internal const val SYMBOL = '@'
    }
}

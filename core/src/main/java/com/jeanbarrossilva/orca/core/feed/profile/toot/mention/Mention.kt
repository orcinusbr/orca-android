package com.jeanbarrossilva.orca.core.feed.profile.toot.mention

import java.io.Serializable
import java.net.URL

/**
 * Reference to a username.
 *
 * @param indices [String]'s [indices][String.indices].
 * @param url [URL] that leads to the owner of the mentioned username.
 **/
data class Mention(val indices: IntRange, val url: URL) : Serializable {
    /** Finds a [Mention]. **/
    abstract class Delimiter {
        /** [Regex] that matches a [Mention]. **/
        internal val regex by lazy(::getRegex)

        /**
         * [Delimiter] that considers [Mention]s parts of a [String] starting with a
         * [Mention.SYMBOL] and determines its end by the absence of alphanumeric or some other
         * special characters.
         **/
        internal object Symbol : Delimiter() {
            override fun getRegex(): Regex {
                return Regex("$SYMBOL[A-Z0-9._%+-]*")
            }

            override fun onGetUsername(match: String): String {
                return match.removePrefix("$SYMBOL")
            }
        }

        /**
         * Gets the username from the [String] region that matches the [regex].
         *
         * @param match Part of the [String] in which the [Mention] has been found.
         **/
        internal fun getUsername(match: String): String {
            return onGetUsername(match)
        }

        /** Gets the [Regex] that matches a [Mention]. **/
        protected abstract fun getRegex(): Regex

        /**
         * Gets the username from the [String] region that matches the [regex].
         *
         * @param match Part of the [String] in which the [Mention] has been found.
         **/
        protected abstract fun onGetUsername(match: String): String
    }

    companion object {
        /** [Char] that indicates the start of a [Mention] by default. **/
        internal const val SYMBOL = '@'
    }
}

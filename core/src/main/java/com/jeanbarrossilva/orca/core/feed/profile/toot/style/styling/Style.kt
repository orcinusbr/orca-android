package com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling

import java.io.Serializable

/** Indicates where and how a target has been stylized. **/
abstract class Style : Serializable {
    /** Indices at which both the style symbols and the target are in the whole [String]. **/
    abstract val indices: IntRange

    /**
     * Describes how a portion of a whole [String] is stylized is applied and identifies the target it's
     * been applied onto.
     **/
    abstract class Delimiter {
        /** Previously applied delimitations to [String]s. **/
        private val delimitations = HashMap<String, List<MatchResult>>()

        /** [Delimiter] that's the root one. **/
        internal val root: Delimiter by lazy { parent?.root ?: this }

        /** [Delimiter] that's this one's parent. **/
        protected abstract val parent: Delimiter?

        /** [Regex] that matches a styled target. **/
        protected abstract val regex: Regex

        /**
         * Delimits the whole [string].
         *
         * @param string [String] with targets to be delimited.
         **/
        internal fun delimit(string: String): List<MatchResult> {
            return delimitations.getOrPut(string) {
                regex.findAll(string).toList()
            }
        }

        /**
         * Gets the target from the [String] region that matches the [regex].
         *
         * @param match Part of the [String] in which the target has been found.
         **/
        internal fun getTarget(match: String): String {
            return onGetTarget(match)
        }

        /**
         * Adds the [Style]'s symbols to the [target].
         *
         * @param target [String] to be targeted.
         **/
        internal fun target(target: String): String {
            return onTarget(target)
        }

        /**
         * Gets the target from the [String] region that matches the [regex].
         *
         * @param match Part of the [String] in which the target has been found.
         **/
        protected abstract fun onGetTarget(match: String): String

        /**
         * Adds the [Style]'s symbols to the [target].
         *
         * @param target [String] to be targeted.
         **/
        protected abstract fun onTarget(target: String): String
    }
}

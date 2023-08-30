package com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling

/** Indicates where and how a target has been stylized. **/
abstract class Style {
    /** [Delimiter] by which this [Style] is delimited. **/
    internal val delimiter by lazy(::getDelimiter)

    /** Indices at which both the style symbols and the target are in the whole [String]. **/
    abstract val indices: IntRange

    /**
     * Describes how a portion of a whole [String] is stylized is applied and identifies the target it's
     * been applied onto.
     **/
    abstract class Delimiter {
        /** [Regex] that matches a styled target. **/
        internal val regex by lazy(::getRegex)

        /**
         * Gets the target from the [String] region that matches the [regex].
         *
         * @param match Part of the [String] in which the target has been found.
         **/
        internal fun getTarget(match: String): String {
            return onGetTarget(match)
        }

        /** Gets the [Regex] that matches a styled target. **/
        protected abstract fun getRegex(): Regex

        /**
         * Gets the target from the [String] region that matches the [regex].
         *
         * @param match Part of the [String] in which the target has been found.
         **/
        protected abstract fun onGetTarget(match: String): String
    }

    /** Gets the [Delimiter] by which this [Style] is delimited. **/
    protected abstract fun getDelimiter(): Delimiter
}

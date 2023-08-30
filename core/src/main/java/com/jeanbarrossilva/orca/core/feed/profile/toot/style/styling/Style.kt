package com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling

/**
 * Describes how a portion of a whole [String] is stylized is applied and identifies the target it's
 * been applied onto.
 **/
abstract class Style {
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

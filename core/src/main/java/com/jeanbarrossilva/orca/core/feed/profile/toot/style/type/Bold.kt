package com.jeanbarrossilva.orca.core.feed.profile.toot.style.type

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.Style

/** [Style] that applies a heavier weight to the target's font. **/
data class Bold(override val indices: IntRange) : Style() {
    /** [Style.Delimiter] for emboldened portions of a [String]. **/
    sealed class Delimiter : Style.Delimiter() {
        /**
         * [Delimiter] that's the [parent] of all [Delimiter.Variant]s and considers [Bold] parts of
         * a [String] surrounded by a [Bold.SYMBOL].
         **/
        internal object Default : Delimiter() {
            /** [Delimiter] to which overridden functionality will be delegated. **/
            private val delegate = surroundedBy(SYMBOL)

            override val parent = delegate.parent

            override fun getRegex(): Regex {
                return delegate.regex
            }

            override fun onGetTarget(match: String): String {
                return delegate.getTarget(match)
            }

            override fun onTarget(target: String): String {
                return delegate.target(target)
            }
        }

        /** [Delimiter] that's a child of [Default]. **/
        abstract class Variant : Delimiter() {
            final override val parent = Default
        }
    }

    companion object {
        /** [Char] that indicates the start and the end of a bold target. **/
        internal const val SYMBOL = '*'
    }
}

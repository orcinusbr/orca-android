package com.jeanbarrossilva.orca.core.feed.profile.toot.style.type

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.Style

/** [Style] for referencing an e-mail, such as "john@appleseed.com". **/
data class Email(override val indices: IntRange) : Style() {
    /** [Style.Delimiter] for [Email]s. **/
    sealed class Delimiter : Style.Delimiter() {
        /** [Delimiter] that's the [parent] of all [Child] instances. **/
        class Parent private constructor() : Delimiter() {
            override val parent = null

            override fun getRegex(): Regex {
                return Email.regex
            }

            override fun onGetTarget(match: String): String {
                return match
            }

            override fun onTarget(target: String): String {
                return target
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
        /** [Regex] that matches an [Email]. **/
        internal val regex = Regex(
            "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\" +
                "x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c" +
                "\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]" +
                "*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-" +
                "4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f" +
                "\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])"
        )
    }
}

package com.jeanbarrossilva.orca.core.feed.profile.toot.style.type

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.Style
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Bold.Delimiter.Default
import java.net.URL

/** [Style] for [URL]s. **/
data class Link(override val indices: IntRange) : Style() {
    /** [Style.Delimiter] for [Link]s. **/
    sealed class Delimiter : Style.Delimiter() {
        /**
         * [Delimiter] that's the [parent] of all [Child] instances and considers [Link]s parts of a
         * [String] conforming to a [URL] format.
         **/
        class Default private constructor() : Delimiter() {
            override val parent = null

            override fun getRegex(): Regex {
                return protocoledRegex
            }

            override fun onGetTarget(match: String): String {
                return match
            }

            override fun onTarget(target: String): String {
                return target
            }

            companion object {
                /** Single [Default] instance. **/
                internal val instance = Default()
            }
        }

        /** [Delimiter] that's a child of [Default]. **/
        abstract class Child : Delimiter() {
            final override val parent = Default.instance
        }
    }

    companion object {
        /** [Regex] that matches the [protocol][URL.getProtocol] of a [URL]. **/
        val protocolRegex = Regex("https?://")

        /** [Regex] that matches the [path][URL.path] of a [URL]. **/
        val pathRegex = Regex("[-a-zA-Z0-9()@:%_+.~#?&/=]+")

        /** [Regex] that matches the subdomain of a URL. **/
        val subdomainRegex = Regex("www\\.")

        /** [Regex] that matches [String]s with an unprotocoled [URL] format. **/
        val unprotocoledRegex = Regex(
            "($subdomainRegex)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}$pathRegex"
        )

        /** [Regex] that matches [String]s with a [URL] format. **/
        val protocoledRegex = Regex("$protocolRegex" + "$unprotocoledRegex")
    }
}

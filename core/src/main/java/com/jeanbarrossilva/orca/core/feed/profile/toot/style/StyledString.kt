package com.jeanbarrossilva.orca.core.feed.profile.toot.style

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Bold
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Hashtag
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Link
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Mention
import java.io.Serializable
import java.net.URL
import java.util.Objects

/**
 * [CharSequence] that supports stylization.
 *
 * @param text Underlying [String] that's been built.
 * @param styles [Style]s applied to the [text].
 **/
class StyledString internal constructor(private val text: String, val styles: List<Style>) :
    CharSequence by text, Serializable {
    constructor(text: String) : this(text, styles = emptyList())

    /**
     * Allows text and [Mention]s to be appended and for a [StyledString] to be built.
     *
     * @see append
     * @see mention
     * @see build
     **/
    class Builder @PublishedApi internal constructor() {
        /**
         * [String] to be the [text][StyledString.text] of the [StyledString] being built.
         **/
        private var text = ""

        /** [Style]s applied to the [text]. **/
        private val styles = mutableListOf<Style>()

        /**
         * Appends the [text] to that of the [StyledString] being built.
         *
         * @param text [Char] to be appended.
         **/
        fun append(text: Char) {
            this.text += text
        }

        /**
         * Appends the [text] to that of the [StyledString] being built.
         *
         * @param text [String] to be appended.
         **/
        fun append(text: String) {
            this.text += text
        }

        /**
         * Emboldens the [text].
         *
         * @param text [String] to be emboldened and appended.
         **/
        fun embolden(text: String) {
            val emboldened = Bold.Delimiter.Parent.instance.target(text)
            val indices = calculateIndicesFor(emboldened)
            val bold = Bold(indices)
            this.text += emboldened
            styles.add(bold)
        }

        /**
         * Appends the [text].
         *
         * @param text [String] to be appended as a [Hashtag].
         **/
        fun hashtag(text: String) {
            val hashTagged = Hashtag.Delimiter.Parent.instance.target(text)
            val indices = calculateIndicesFor(hashTagged)
            val hashtag = Hashtag(indices)
            this.text += hashTagged
            styles.add(hashtag)
        }

        /**
         * Links to the [url].
         *
         * @param url [URL] to be appended and linked to.
         **/
        fun linkTo(url: URL) {
            val linked = Link.Delimiter.Parent.instance.target("$url")
            val indices = calculateIndicesFor(linked)
            val link = Link(indices)
            this.text += linked
            styles.add(link)
        }

        /**
         * Mentions a [username] whose owner can be found by the [url].
         *
         * @param username Username to mention.
         * @param url [URL] that leads to the [username]'s owner.
         * @see Mention
         **/
        fun mention(username: String, url: URL) {
            val mentioned = Mention.Delimiter.Parent.instance.target(username)
            val indices = calculateIndicesFor(mentioned)
            val mention = Mention(indices, url)
            this.text += mentioned
            styles.add(mention)
        }

        /** Builds a [StyledString] with the provided styles. **/
        @PublishedApi
        internal fun build(): StyledString {
            val mentionsAsList = styles.toList()
            return StyledString(text, mentionsAsList)
        }

        /** Calculates the indices at which the [text] will be when appended. **/
        private fun calculateIndicesFor(text: String): IntRange {
            return this.text.length..(this.text.length + text.lastIndex)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is String && toString() == other ||
            other is StyledString &&
            text == other.text &&
            styles.containsAll(other.styles)
    }

    override fun hashCode(): Int {
        return Objects.hash(text, styles)
    }

    override fun toString(): String {
        return text
    }

    companion object {
        /**
         * Normalizes the [string] whose [Style]s are delimited by the specified [delimiters].
         *
         * @param string [String] to be normalized.
         * @param delimiters [Style.Delimiter] by which the [String]'s [Style]s are delimited.
         **/
        internal fun normalize(string: String, vararg delimiters: Style.Delimiter): String {
            var normalized = string
            val delimiterIterator = delimiters.iterator()
            while (delimiterIterator.hasNext()) {
                normalized = normalize(normalized, delimiterIterator.next())
            }
            return normalized
        }

        /**
         * Normalizes the [string] whose respective [Style] is delimited by the specified
         * [delimiter].
         *
         * @param string [String] to be normalized.
         * @param delimiter [Style.Delimiter] by which the [String]'s [Style] is delimited.
         **/
        internal fun normalize(string: String, delimiter: Style.Delimiter): String {
            return buildString {
                append(string)
                delimiter.delimit(string).forEach {
                    replace(
                        it.range.first,
                        it.range.last.inc(),
                        (delimiter.parent ?: delimiter).target(delimiter.getTarget(it.value))
                    )
                }
            }
        }
    }
}

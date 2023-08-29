package com.jeanbarrossilva.orca.core.feed.profile.toot.mention

import java.io.Serializable
import java.net.URL
import java.util.Objects

/**
 * [CharSequence] that supports references to others' usernames.
 *
 * @param text Underlying [String] that's been built.
 * @param mentions [Mention]s in the [text].
 **/
class MentionableString internal constructor(
    private val text: String,
    val mentions: List<Mention>
) : CharSequence by text, Serializable {
    constructor(text: String) : this(text, mentions = emptyList())

    /**
     * Allows text and [Mention]s to be appended and for a [MentionableString] to be built.
     *
     * @see append
     * @see mention
     * @see build
     **/
    class Builder @PublishedApi internal constructor() {
        /**
         * [String] to be the [text][MentionableString.text] of the [MentionableString] being built.
         **/
        private var text = ""

        /** [Mention]s associated to the [text]. **/
        private var mentions = mutableListOf<Mention>()

        /**
         * Appends the [text] to that of the [MentionableString] being built.
         *
         * @param text [Char] to be appended.
         **/
        fun append(text: Char) {
            this.text += text
        }

        /**
         * Appends the [text] to that of the [MentionableString] being built.
         *
         * @param text [String] to be appended.
         **/
        fun append(text: String) {
            this.text += text
        }

        /**
         * Mentions a [username] whose owner can be found by the [url].
         *
         * @param username Username to mention.
         * @param url [URL] that leads to the [username]'s owner.
         * @see Mention
         **/
        fun mention(username: String, url: URL) {
            val text = Mention.SYMBOL + username
            val indices = this.text.length..(this.text.length + text.lastIndex)
            val mention = Mention(indices, url)
            this.text += text
            mentions.add(mention)
        }

        /** Builds a [MentionableString] with the provided configuration. **/
        @PublishedApi
        internal fun build(): MentionableString {
            val mentionsAsList = mentions.toList()
            return MentionableString(text, mentionsAsList)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is String && toString() == other ||
            other is MentionableString &&
            text == other.text &&
            mentions == other.mentions
    }

    override fun hashCode(): Int {
        return Objects.hash(text, mentions)
    }

    override fun toString(): String {
        return text
    }

    companion object {
        /**
         * Normalizes the [string] whose [Mention]s are delimited by the specified [delimiter]; that
         * is, formats its [Mention]s so that they match the default format.
         *
         * @param string [String] to be normalized.
         * @param delimiter [Mention.Delimiter] by which the [string]'s [Mention]s are delimited.
         * @see Mention.Delimiter.Symbol
         **/
        internal fun normalize(string: String, delimiter: Mention.Delimiter): String {
            return buildString {
                append(string)
                delimiter.regex.findAll(this).forEach {
                    replace(
                        it.range.first,
                        it.range.last.inc(),
                        Mention.SYMBOL + delimiter.getUsername(it.value)
                    )
                }
            }
        }
    }
}

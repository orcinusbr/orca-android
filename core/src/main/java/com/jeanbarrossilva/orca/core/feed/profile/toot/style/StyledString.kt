package com.jeanbarrossilva.orca.core.feed.profile.toot.style

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Bold
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Email
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Hashtag
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Italic
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Link
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Mention
import java.io.Serializable
import java.net.URL
import java.util.Objects

/**
 * [CharSequence] that supports stylization such as `*bold*`, e-mails (`orca@jeanbarrossilva.com`),
 * `#hashtags`, `_italic_`, links (`https://orca.jeanbarrossilva.com`) and `@mentions`.
 *
 * Those can either be added through the [buildStyledString] builder method or by converting a
 * [String] properly delimited with the [Style]s' [delimiter][Style.Delimiter]s into a
 * [StyledString] with [String.toStyledString]. When converting, custom
 * [delimiter][Style.Delimiter]s can be provided if the [String] contains a
 * [delimiter][Style.Delimiter] for any of the [Style]s that's different from the default ones.
 *
 * @param text Underlying [String] that's been built.
 * @param styles [Style]s applied to the [text].
 **/
class StyledString internal constructor(private val text: String, val styles: List<Style>) :
    CharSequence by text, Serializable {
    /**
     * [StyledString] without [Style]s.
     *
     * @param text [String] without any [Style]s attached to it.
     **/
    constructor(text: String) : this(text, styles = emptyList())

    /**
     * Allows text and [Style]s to be appended and for a [StyledString] to be built.
     *
     * @see append
     * @see bold
     * @see hashtag
     * @see italic
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

        /** [Appender]s that are currently active. **/
        private val activeAppenders = mutableListOf<Appender>()

        /**
         * Appends text with the result of [style] applied to it through [append].
         *
         * @param delimiter [Style.Delimiter] by which the result of [style] applied to the text
         * will be delimited.
         * @param style Creates a [Style] based on the given indices at which it'll be applied.
         **/
        inner class Appender internal constructor(
            private val delimiter: Style.Delimiter,
            private val style: (indices: IntRange) -> Style
        ) {
            /**
             * Appends this [Char], stylizing it with the result of [style] and the [Style]s of
             * [Appender]s that are currently active.
             **/
            operator fun Char.unaryPlus() {
                +toString()
            }

            /**
             * Appends this [String], stylizing it with the result of [style] and the [Style]s of
             * [Appender]s that are currently active.
             **/
            operator fun String.unaryPlus() {
                val stylized = delimiter.target(this)
                require(stylized matches delimiter.regex) { "\"$stylized\" is invalid." }
                val indices = calculateIndicesFor(stylized)
                val styles = activeAppenders.map { it.style(indices) }
                with(this@Builder) { +stylized }
                this@Builder.styles.addAll(styles)
            }

            /** Calculates the indices at which the [text] will be when appended. **/
            private fun calculateIndicesFor(text: String): IntRange {
                return this@Builder.text.length..(this@Builder.text.length + text.lastIndex)
            }
        }

        /**
         * Appends this [Char] to the [text][StyledString.text] of the [StyledString] being built.
         **/
        operator fun Char.unaryPlus() {
            text += this
        }

        /**
         * Appends this [String] to the [text][StyledString.text] of the [StyledString] being built.
         **/
        operator fun String.unaryPlus() {
            val emails = text.stylize(Email.Delimiter.Default, ::Email)
            val links = text.stylize(Link.Delimiter.Default, ::Link)
            text += this
            styles.addAll(emails + links)
        }

        /**
         * Emboldens the text to be appended.
         *
         * @param appendix Additionally stylizes the bold text to be appended or solely appends it.
         * @see Bold
         **/
        fun bold(appendix: Appender.() -> Unit) {
            append(Bold.Delimiter.Default, appendix, ::Bold)
        }

        /**
         * Turns the text to be appended into a hashtag.
         *
         * @param appendix Additionally stylizes the hashtag to be appended or solely appends it.
         * @see Hashtag
         **/
        fun hashtag(appendix: Appender.() -> Unit) {
            append(Hashtag.Delimiter.Default, appendix, ::Hashtag)
        }

        /**
         * Italicizes the text to be appended.
         *
         * @param appendix Additionally stylizes the italic text to be appended or solely appends
         * it.
         * @see Italic
         **/
        fun italic(appendix: Appender.() -> Unit) {
            append(Italic.Delimiter.Default, appendix, ::Italic)
        }

        /**
         * Turns the text to be appended into a mention.
         *
         * @param appendix Additionally stylizes the mention to be appended or solely appends it.
         * @see Mention
         **/
        fun mention(url: URL, appendix: Appender.() -> Unit) {
            append(Mention.Delimiter.Default, appendix) {
                Mention(it, url)
            }
        }

        /** Builds a [StyledString] with the provided styles. **/
        @PublishedApi
        internal fun build(): StyledString {
            val mentionsAsList = styles.toList()
            return StyledString(text, mentionsAsList)
        }

        /**
         * Creates an [Appender] and runs [appendix] on it, making it active for as long as it's
         * being used.
         *
         * @param delimiter [Style.Delimiter] by which the result of [style] applied to the text
         * will be delimited.
         * @param appendix Additionally stylizes the text to be appended or solely appends it.
         * @param style Creates the [Style] to be applied to the appended text at the specified
         * indices.
         **/
        private fun append(
            delimiter: Style.Delimiter,
            appendix: Appender.() -> Unit,
            style: (IntRange) -> Style
        ) {
            val appender = Appender(delimiter, style)
            activeAppenders.add(appender)
            appender.appendix()
            activeAppenders.remove(appender)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is String &&
            toString() == other ||
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
        internal fun normalize(string: String, vararg delimiters: Style.Delimiter?): String {
            var normalized = string
            val delimiterIterator = delimiters.filterNotNull().iterator()
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
            var normalized = string
            delimiter.delimit(normalized).forEach {
                normalized = normalized.replaceRange(
                    it.range.first..it.range.last,
                    delimiter.root.target(delimiter.getTarget(it.value))
                )
            }
            return normalized
        }
    }
}

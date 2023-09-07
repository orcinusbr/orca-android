package com.jeanbarrossilva.orca.core.feed.profile.toot.content

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Headline
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.HeadlineProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import com.jeanbarrossilva.orca.std.styledstring.toStyledString
import com.jeanbarrossilva.orca.std.styledstring.type.Link
import java.net.URL
import java.util.Objects

/**
 * Part that's been composed by the user.
 *
 * @param text Written content.
 * @param highlight [Highlight] from the [text].
 **/
class Content private constructor(val text: StyledString, val highlight: Highlight? = null) {
    /**
     * [IllegalArgumentException] thrown if the text has a [Highlight] while a [Headline] hasn't
     * been provided.
     **/
    class UnprovidedHeadlineException :
        IllegalArgumentException("Text has a highlight but no headline was provided.")

    override fun equals(other: Any?): Boolean {
        return other is Content && text == other.text && highlight == other.highlight
    }

    override fun hashCode(): Int {
        return Objects.hash(text, highlight)
    }

    override fun toString(): String {
        return "Content(text=$text, highlight=$highlight)"
    }

    companion object {
        /**
         * Creates [Content] from the given [text].
         *
         * @param text [String] from which [Content] will be created.
         * @param headlineProvider [HeadlineProvider] that provides the [Headline].
         * @throws UnprovidedHeadlineException If the text has a [Highlight] while a
         * [HeadlineProvider] hasn't been specified.
         **/
        @Throws(UnprovidedHeadlineException::class)
        fun from(text: StyledString, headlineProvider: HeadlineProvider): Content {
            val links = text.styles.filterIsInstance<Link>()
            val link = links.firstOrNull()
            val url = link?.indices?.let(text::substring)?.let(::URL)
            val headline = url?.let { headlineProvider.provide(it) }
            val highlight = headline?.let { Highlight(it, url) }
            val formattedText = if (links.size == 1 && text.trim().endsWith("$url")) {
                "${text.replaceRange(link?.indices ?: IntRange.EMPTY, "")}"
                    .trimEnd()
                    .toStyledString()
            } else {
                text
            }
            return Content(formattedText, highlight)
        }

        /**
         * Ensures the parity of an operation involving the [link] and the [headline], throwing if
         * the [link] is not `null` and the [headline] is `null`.
         *
         * @param link [Link] found in a [StyledString].
         * @param headline [Headline] with main information about a [Highlight].
         * @throws UnprovidedHeadlineException If the text has a [Highlight] while the [headline]
         * hasn't been specified.
         **/

        /*
         * There's a case in which the link will exist and a headline won't be provided: when the
         * URL leads to an in-app resource (such as a toot or a profile). Because these links start
         * with the instance base URL, a solution for obtaining instance-specific URL resources must
         * be developed in order for parity between the link and the headline to be properly
         * ensured.
         */
        @Throws(UnprovidedHeadlineException::class)
        private fun ensureParity(link: Link?, headline: Headline?) {
            if (link != null && headline == null) {
                throw UnprovidedHeadlineException()
            }
        }
    }
}

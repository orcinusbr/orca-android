package com.jeanbarrossilva.orca.core.feed.profile.toot.style

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Bold
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Email
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Hashtag
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Italic
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Link
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Mention
import java.net.URL

/**
 * Converts this [String] into a [StyledString].
 *
 * @param boldDelimiter [Bold.Delimiter] by which this [String]'s emboldened portions are delimited.
 * @param hashtagDelimiter [Hashtag.Delimiter] by which this [String]'s [Hashtag]s are delimited.
 * @param italicDelimiter [Italic.Delimiter] by which this [String]'s italicized portions are
 * delimited.
 * @param linkDelimiter [Link.Delimiter] by which this [String]'s [Link]s are delimited.
 * @param mentionDelimiter [Mention.Delimiter] by which this [String]'s [Mention]s are delimited.
 * @param mentioning Given its start index, maps each mention to a username to a [URL].
 * @see Bold
 * @see Italic
 **/
fun String.toStyledString(
    boldDelimiter: Bold.Delimiter? = Bold.Delimiter.Default.instance,
    hashtagDelimiter: Hashtag.Delimiter? = Hashtag.Delimiter.Default.instance,
    italicDelimiter: Italic.Delimiter? = Italic.Delimiter.Default.instance,
    linkDelimiter: Link.Delimiter? = Link.Delimiter.Default.instance,
    mentionDelimiter: Mention.Delimiter? = Mention.Delimiter.Default.instance,
    mentioning: (startIndex: Int) -> URL? = { null }
): StyledString {
    val text = StyledString.normalize(
        this,
        boldDelimiter,
        Email.Delimiter.Default.instance,
        hashtagDelimiter,
        italicDelimiter,
        linkDelimiter,
        mentionDelimiter
    )
    val emboldened = text.stylize(Bold.Delimiter.Default.instance, ::Bold)
    val emails = text.stylize(Email.Delimiter.Default.instance, ::Email)
    val hashtags = text.stylize(Hashtag.Delimiter.Default.instance, ::Hashtag)
    val italicized = text.stylize(Italic.Delimiter.Default.instance, ::Italic)
    val links = text.stylize(Link.Delimiter.Default.instance, ::Link)
    val mentions = text.stylize(Mention.Delimiter.Default.instance) {
        mentioning(it.first)?.let { url ->
            Mention(indices = it, url)
        }
    }
    return StyledString(text, emails + emboldened + hashtags + italicized + links + mentions)
}

/**
 * Gets the [Style]s present in this [String] according to the [delimiter].
 *
 * @param delimiter [Style.Delimiter] by which the [Style]s to be obtained are delimited.
 * @param style Returns the respective [Style] applied to the given [indices] of this [String].
 **/
private fun <T : Style> String.stylize(
    delimiter: Style.Delimiter?,
    style: (indices: IntRange) -> T?
): List<T> {
    return delimiter?.delimit(this)?.mapNotNull { style(it.range) }.orEmpty()
}

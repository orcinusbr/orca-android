package com.jeanbarrossilva.orca.core.feed.profile.toot.style

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Bold
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Email
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Hashtag
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Link
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Mention
import java.net.URL

/**
 * Converts this [String] into a [StyledString].
 *
 * @param boldDelimiter [Bold.Delimiter] by which this [String]'s emboldened portions are delimited.
 * @param hashtagDelimiter [Hashtag.Delimiter] by which this [String]'s [Hashtag]s are delimited.
 * @param linkDelimiter [Link.Delimiter] by which this [String]'s [Link]s are delimited.
 * @param mentionDelimiter [Mention.Delimiter] by which this [String]'s [Mention]s are delimited.
 * @param mentioning Given its start index, maps each mention to a username to a [URL].
 * @see indices
 **/
fun String.toStyledString(
    boldDelimiter: Bold.Delimiter? = Bold.Delimiter.Parent.instance,
    hashtagDelimiter: Hashtag.Delimiter? = Hashtag.Delimiter.Parent.instance,
    linkDelimiter: Link.Delimiter? = Link.Delimiter.Parent.instance,
    mentionDelimiter: Mention.Delimiter? = Mention.Delimiter.Parent.instance,
    mentioning: (startIndex: Int) -> URL? = { null }
): StyledString {
    val text = StyledString.normalize(
        this,
        boldDelimiter,
        Email.Delimiter.Parent.instance,
        hashtagDelimiter,
        linkDelimiter,
        mentionDelimiter
    )
    val emails = Email.Delimiter.Parent.instance.delimit(text).map { Email(indices = it.range) }
    val emboldened = Bold.Delimiter.Parent.instance.delimit(text).map { Bold(indices = it.range) }
    val hashtags =
        Hashtag.Delimiter.Parent.instance.delimit(text).map { Hashtag(indices = it.range) }
    val links = Link.Delimiter.Parent.instance.delimit(text).map { Link(indices = it.range) }
    val mentions = Mention.Delimiter.Parent.instance.delimit(text).mapNotNull { match ->
        mentioning(match.range.first)?.let { url ->
            Mention(indices = match.range, url)
        }
    }
    return StyledString(text, emails + emboldened + hashtags + links + mentions)
}

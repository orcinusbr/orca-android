package com.jeanbarrossilva.orca.core.feed.profile.toot.style

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Bold
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Mention
import java.net.URL

/**
 * Converts this [String] into a [StyledString].
 *
 * @param boldDelimiter [Bold.Delimiter] by which this [String]'s emboldened portions are delimited.
 * @param mentionDelimiter [Mention.Delimiter] by which this [String]'s [Mention]s are delimited.
 * @param mentioning Given its start index, maps each mention to a username to a [URL].
 * @see indices
 **/
fun String.toStyledString(
    boldDelimiter: Bold.Delimiter = Bold.Delimiter.Parent.instance,
    mentionDelimiter: Mention.Delimiter = Mention.Delimiter.Parent.instance,
    mentioning: (startIndex: Int) -> URL? = { null }
): StyledString {
    val text = StyledString.normalize(this, boldDelimiter, mentionDelimiter)
    val emboldened = Bold.Delimiter.Parent.instance.delimit(text).map { Bold(indices = it.range) }
    val mentions = Mention.Delimiter.Parent.instance.delimit(text).mapNotNull { match ->
        mentioning(match.range.first)?.let { url ->
            Mention(match.range, url)
        }
    }
    return StyledString(text, emboldened + mentions)
}

package com.jeanbarrossilva.orca.core.feed.profile.toot.style

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.Mention
import java.net.URL

/**
 * Converts this [String] into a [StyledString].
 *
 * @param delimiter [Mention.Delimiter] for determining where a [Mention] starts and where it ends.
 * @param link Given its start index, maps each mention to a username to a [URL].
 * @see Mention.indices
 **/
fun String.toStyledString(
    delimiter: Mention.Delimiter = Mention.Delimiter.Symbol,
    link: (startIndex: Int) -> URL? = { null }
): StyledString {
    val text = StyledString.normalize(this, delimiter)
    val mentions = Mention
        .Delimiter
        .Symbol
        .regex
        .findAll(text)
        .toList()
        .map(MatchResult::range)
        .mapNotNull { range ->
            link(range.first)?.let { url ->
                Mention(range, url)
            }
        }
    return StyledString(text, mentions)
}

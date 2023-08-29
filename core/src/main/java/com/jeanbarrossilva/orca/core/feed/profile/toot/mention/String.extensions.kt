package com.jeanbarrossilva.orca.core.feed.profile.toot.mention

import java.net.URL

/**
 * Converts this [String] into a [MentionableString].
 *
 * @param delimiter [Mention.Delimiter] for determining where a [Mention] starts and where it ends.
 * @param link Given its start index, maps each mention to a username to a [URL].
 * @see Mention.indices
 **/
fun String.toMentionableString(
    delimiter: Mention.Delimiter = Mention.Delimiter.Symbol,
    link: (startIndex: Int) -> URL? = { null }
): MentionableString {
    val text = MentionableString.normalize(this, delimiter)
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
    return MentionableString(text, mentions)
}

package com.jeanbarrossilva.orca.core.feed.profile.toot.style

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.Style
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention.Mention
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention.SymbolMentionDelimiter
import java.net.URL

/**
 * Converts this [String] into a [StyledString].
 *
 * @param mentionDelimiter [Style.Delimiter] by which this [String]'s [Mention]s are delimited.
 * @param mentioning Given its start index, maps each mention to a username to a [URL].
 * @see indices
 **/
fun String.toStyledString(
    mentionDelimiter: Style.Delimiter = SymbolMentionDelimiter,
    mentioning: (startIndex: Int) -> URL? = { null }
): StyledString {
    val text = StyledString.normalize(this, mentionDelimiter)
    val mentions = SymbolMentionDelimiter
        .regex
        .findAll(text)
        .toList()
        .map(MatchResult::range)
        .mapNotNull { range ->
            mentioning(range.first)?.let { url ->
                Mention(mentionDelimiter, range, url)
            }
        }
    return StyledString(text, mentions)
}

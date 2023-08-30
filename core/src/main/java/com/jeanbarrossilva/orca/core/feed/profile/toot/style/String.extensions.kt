package com.jeanbarrossilva.orca.core.feed.profile.toot.style

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.Style
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention.Mention
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention.SymbolMentionStyle
import java.net.URL

/**
 * Converts this [String] into a [StyledString].
 *
 * @param mentionStyle [Style] for determining where a [Mention] starts and where it ends.
 * @param mentioning Given its start index, maps each mention to a username to a [URL].
 * @see Mention.indices
 **/
fun String.toStyledString(
    mentionStyle: Style = SymbolMentionStyle,
    mentioning: (startIndex: Int) -> URL? = { null }
): StyledString {
    val text = StyledString.normalize(this, mentionStyle)
    val mentions = SymbolMentionStyle
        .regex
        .findAll(text)
        .toList()
        .map(MatchResult::range)
        .mapNotNull { range ->
            mentioning(range.first)?.let { url ->
                Mention(range, url)
            }
        }
    return StyledString(text, mentions)
}

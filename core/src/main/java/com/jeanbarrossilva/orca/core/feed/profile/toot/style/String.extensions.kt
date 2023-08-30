package com.jeanbarrossilva.orca.core.feed.profile.toot.style

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.Style
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.bold.Bold
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.bold.BoldDelimiter
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention.Mention
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention.MentionDelimiter
import java.net.URL

/**
 * Converts this [String] into a [StyledString].
 *
 * @param boldDelimiter [Style.Delimiter] by which this [String]'s emboldened portions are
 * delimited.
 * @param mentionDelimiter [Style.Delimiter] by which this [String]'s [Mention]s are delimited.
 * @param mentioning Given its start index, maps each mention to a username to a [URL].
 * @see indices
 **/
fun String.toStyledString(
    boldDelimiter: BoldDelimiter = BoldDelimiter.Parent.instance,
    mentionDelimiter: Style.Delimiter = MentionDelimiter.Parent.instance,
    mentioning: (startIndex: Int) -> URL? = { null }
): StyledString {
    val text = StyledString.normalize(this, boldDelimiter, mentionDelimiter)
    val emboldened = BoldDelimiter.Parent.instance.delimit(text).map { Bold(indices = it.range) }
    val mentions = MentionDelimiter.Parent.instance.delimit(text).mapNotNull { match ->
        mentioning(match.range.first)?.let { url ->
            Mention(match.range, url)
        }
    }
    return StyledString(text, emboldened + mentions)
}

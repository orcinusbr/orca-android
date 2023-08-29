package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.mention

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.jeanbarrossilva.orca.core.feed.profile.toot.mention.MentionableString
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.html.span.converter.URLSpanConverter

/**
 * Converts this [MentionableString] into an [AnnotatedString].
 *
 * @param colors [Colors] by which the [AnnotatedString] can be colored.
 **/
fun MentionableString.toAnnotatedString(colors: Colors): AnnotatedString {
    return buildAnnotatedString {
        append(this@toAnnotatedString)
        mentions.forEach {
            addStyle(URLSpanConverter.getSpanStyle(colors), it.indices.first, it.indices.last)
        }
    }
}

package com.jeanbarrossilva.orca.platform.ui.core

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.Style
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Bold
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Hashtag
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Link
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Mention
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors

/**
 * Converts this [Style] into a [SpanStyle].
 *
 * @param colors [Colors] by which the resulting [SpanStyle] can be colored.
 * @throws IllegalArgumentException If the [Style] is unknown.
 **/
@Throws(IllegalArgumentException::class)
internal fun Style.toSpanStyle(colors: Colors): SpanStyle {
    return when (this) {
        is Bold -> SpanStyle(fontWeight = FontWeight.Bold)
        is Hashtag, is Link, is Mention -> SpanStyle(colors.brand.container)
        else -> throw IllegalArgumentException("Cannot convert an unknown $this style.")
    }
}

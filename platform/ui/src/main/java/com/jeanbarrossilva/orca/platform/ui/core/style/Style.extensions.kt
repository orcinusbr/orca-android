package com.jeanbarrossilva.orca.platform.ui.core.style

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.std.styledstring.Style
import com.jeanbarrossilva.orca.std.styledstring.type.Bold
import com.jeanbarrossilva.orca.std.styledstring.type.Email
import com.jeanbarrossilva.orca.std.styledstring.type.Hashtag
import com.jeanbarrossilva.orca.std.styledstring.type.Link
import com.jeanbarrossilva.orca.std.styledstring.type.Mention

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
        is Email, is Hashtag, is Link, is Mention -> SpanStyle(colors.brand.container)
        else -> throw IllegalArgumentException("Cannot convert an unknown $this style.")
    }
}

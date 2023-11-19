package com.jeanbarrossilva.orca.platform.ui.core.style

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.std.styledstring.style.Style
import com.jeanbarrossilva.orca.std.styledstring.style.type.Bold
import com.jeanbarrossilva.orca.std.styledstring.style.type.Email
import com.jeanbarrossilva.orca.std.styledstring.style.type.Hashtag
import com.jeanbarrossilva.orca.std.styledstring.style.type.Italic
import com.jeanbarrossilva.orca.std.styledstring.style.type.Link
import com.jeanbarrossilva.orca.std.styledstring.style.type.Mention

/**
 * Converts this [Style] into a [SpanStyle].
 *
 * @param colors [Colors] by which the resulting [SpanStyle] can be colored.
 * @throws IllegalArgumentException If the [Style] is unknown.
 */
@Throws(IllegalArgumentException::class)
internal fun Style.toSpanStyle(colors: Colors): SpanStyle {
  return when (this) {
    is Bold -> SpanStyle(fontWeight = FontWeight.Bold)
    is Email,
    is Hashtag,
    is Link,
    is Mention -> SpanStyle(colors.link.asColor)
    is Italic -> SpanStyle(fontStyle = FontStyle.Italic)
    else -> throw IllegalArgumentException("Cannot convert an unknown $this style.")
  }
}

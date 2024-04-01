/*
 * Copyright © 2023-2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.composite.timeline.text.annotated.span

import androidx.compose.ui.text.SpanStyle
import br.com.orcinus.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.composite.timeline.text.annotated.span.category.Categorizer
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
    is Bold -> BoldSpanStyle
    is Email -> createLinkSpanStyle(colors, Categorizer.categorizeAsEmail())
    is Hashtag -> createLinkSpanStyle(colors, Categorizer.categorizeAsHashtag())
    is Italic -> ItalicSpanStyle
    is Mention -> createLinkSpanStyle(colors, Categorizer.categorizeAsMention(url))
    is Link -> createLinkSpanStyle(colors, Categorizer.categorizeAsLink(url))
    else -> throw IllegalArgumentException("Cannot convert an unknown $this style.")
  }
}

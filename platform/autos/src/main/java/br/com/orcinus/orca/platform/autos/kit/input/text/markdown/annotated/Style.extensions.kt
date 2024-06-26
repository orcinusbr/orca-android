/*
 * Copyright © 2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.platform.autos.kit.input.text.markdown.annotated

import androidx.annotation.VisibleForTesting
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.Style
import java.net.URI

/**
 * Denotes the start of a [SpanStyle]'s font feature setting category created from a [Style.Link].
 *
 * @see CATEGORY_SUFFIX
 */
private const val CATEGORY_PREFIX = "category: url("

/**
 * Denotes the end of a [SpanStyle]'s font feature setting category created from a [Style.Link].
 *
 * @see CATEGORY_PREFIX
 */
private const val CATEGORY_SUFFIX = ")"

/**
 * [SpanStyle] into which an emboldened portion within [Markdown] will be turned when converting it
 * into an [AnnotatedString].
 *
 * @see Style.Bold
 * @see toAnnotatedString
 */
@VisibleForTesting internal val BoldSpanStyle = SpanStyle(fontWeight = FontWeight.Bold)

/**
 * [SpanStyle] into which an italicized portion within [Markdown] will be turned when converting it
 * into an [AnnotatedString].
 *
 * @see Style.Italic
 * @see toAnnotatedString
 */
@VisibleForTesting internal val ItalicSpanStyle = SpanStyle(fontStyle = FontStyle.Italic)

/**
 * Converts this [Style] into a [SpanStyle].
 *
 * @param colors [Colors] by which the resulting [SpanStyle] can be colored.
 * @throws IllegalArgumentException If the [Style] is unknown.
 */
@Throws(IllegalArgumentException::class)
internal fun Style.toSpanStyle(colors: Colors): SpanStyle {
  return when (this) {
    is Style.Bold -> BoldSpanStyle
    is Style.Italic -> ItalicSpanStyle
    is Style.Link -> createLinkSpanStyle(colors, uri)
  }
}

/**
 * Creates a [SpanStyle] into which a [Style.Link] within [Markdown] will be turned when converting
 * it into an [AnnotatedString].
 *
 * @param colors [Colors] by which the [SpanStyle] can be colored.
 * @param uri [URI] to which the [Style.Link] links.
 * @see toAnnotatedString
 * @see category
 */
private fun createLinkSpanStyle(colors: Colors, uri: URI): SpanStyle {
  return SpanStyle(
    Color(colors.link),
    fontFeatureSettings = CATEGORY_PREFIX + uri + CATEGORY_SUFFIX
  )
}

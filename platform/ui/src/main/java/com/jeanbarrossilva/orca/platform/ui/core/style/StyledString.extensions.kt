/*
 * Copyright © 2023 Orca
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

package com.jeanbarrossilva.orca.platform.ui.core.style

import android.text.Html
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.core.style.spanned.toStyledString
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import com.jeanbarrossilva.orca.std.styledstring.style.Style
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

/**
 * Creates a [StyledString] from the [html].
 *
 * @param html HTML-formatted [String] from which a [StyledString] will be created.
 */
fun StyledString.Companion.fromHtml(html: String): StyledString {
  return Html.fromHtml(
      Jsoup.parse(html, Parser.xmlParser())
        .apply {
          /*
           * Last paragraph is popped because `Html#fromHtml` would append trailing line breaks
           * otherwise.
           */
          children().tagName("p").lastOrNull()?.pop()
        }
        .html(),
      Html.FROM_HTML_MODE_LEGACY
    )
    .toStyledString()
}

/** Converts this [StyledString] into an [AnnotatedString]. */
@Composable
fun StyledString.toAnnotatedString(): AnnotatedString {
  return toAnnotatedString(AutosTheme.colors)
}

/**
 * Converts this [StyledString] into an [AnnotatedString].
 *
 * @param colors [Colors] by which the [AnnotatedString] can be colored.
 */
fun StyledString.toAnnotatedString(colors: Colors): AnnotatedString {
  val conversions = HashMap<Style, SpanStyle>()
  return buildAnnotatedString {
    append(this@toAnnotatedString)
    styles.forEach {
      addStyle(
        conversions.getOrPut(it) { it.toSpanStyle(colors) },
        it.indices.first,
        it.indices.last.inc()
      )
    }
  }
}

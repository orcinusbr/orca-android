/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.composite.timeline.text.annotated

import android.content.Context
import android.text.Html
import android.text.Spanned
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import br.com.orcinus.orca.composite.timeline.text.pop
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.markdown.annotated.toAnnotatedString
import br.com.orcinus.orca.platform.markdown.spanned.toMarkdown
import br.com.orcinus.orca.std.markdown.Markdown
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

/**
 * Creates [Markdown] from the [html].
 *
 * @param context [Context] with which the underlying [Spanned] will be converted into [Markdown].
 * @param html HTML-formatted [String] from which [Markdown] will be created.
 * @see Spanned.toMarkdown
 */
fun Markdown.Companion.fromHtml(context: Context, html: String): Markdown {
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
    .toMarkdown(context)
}

/** Converts this [Markdown] into an [AnnotatedString]. */
@Composable
fun Markdown.toAnnotatedString(): AnnotatedString {
  return toAnnotatedString(AutosTheme.colors)
}

/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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

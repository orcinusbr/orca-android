package com.jeanbarrossilva.orca.platform.ui.core.style

import android.text.Html
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.std.styledstring.Style
import com.jeanbarrossilva.orca.std.styledstring.StyledString

/**
 * Creates a [StyledString] from the [html].
 *
 * @param html HTML-formatted [String] from which a [StyledString] will be created.
 */
fun StyledString.Companion.fromHtml(html: String): StyledString {
  val paragraphLessHtml = html.replace("<p>", "").replace("</p>", "")
  return Html.fromHtml(paragraphLessHtml, Html.FROM_HTML_MODE_COMPACT).toStyledString()
}

/** Converts this [StyledString] into an [AnnotatedString]. */
@Composable
fun StyledString.toAnnotatedString(): AnnotatedString {
  return toAnnotatedString(OrcaTheme.colors)
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

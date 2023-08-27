package com.jeanbarrossilva.orca.platform.ui.html

import android.text.Html
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.html.span.SpanStyle

/**
 * Creates an [AnnotatedString] from the HTML [text].
 *
 * @param text HTML content.
 **/
@Composable
@Suppress("ComposableNaming")
fun HtmlAnnotatedString(text: String): AnnotatedString {
    return HtmlAnnotatedString(OrcaTheme.colors, text)
}

/**
 * Creates an [AnnotatedString] from the HTML [text].
 *
 * @param colors [Colors] by which the resulting [AnnotatedString] will be colored.
 * @param text HTML content.
 **/
@Suppress("FunctionName")
fun HtmlAnnotatedString(colors: Colors, text: String): AnnotatedString {
    /*
     * Having the paragraph tags adds a line break to the end of the Spanned that's returned by
     * Html.fromHtml.
     */
    val paragraphLessText = text.replace(Regex("<p>|</p>"), "")

    return buildAnnotatedString {
        Html.fromHtml(paragraphLessText, Html.FROM_HTML_MODE_COMPACT).run {
            forEachIndexed { index, char ->
                val spans = getSpans(index, index, Object::class.java)
                val spanStyle = SpanStyle(colors, spans)
                withStyle(spanStyle) { append(char) }
            }
        }
    }
}

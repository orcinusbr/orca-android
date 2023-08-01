package com.jeanbarrossilva.orca.platform.ui.html

import android.text.Html
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.jeanbarrossilva.orca.platform.ui.html.span.SpanStyle

/**
 * Creates an [AnnotatedString] from the HTML [text].
 *
 * @param text HTML content.
 **/
@Suppress("FunctionName")
fun HtmlAnnotatedString(text: String): AnnotatedString {
    /*
     * Having the paragraph tags adds a line break to the end of the Spanned that's returned by
     * Html.fromHtml.
     */
    val paragraphLessText = text.replace(Regex("<p>|</p>"), "")

    return buildAnnotatedString {
        Html.fromHtml(paragraphLessText, Html.FROM_HTML_MODE_COMPACT).run {
            forEachIndexed { index, char ->
                val spans = getSpans(index, index, Object::class.java)
                val spanStyle = SpanStyle(spans)
                withStyle(spanStyle) { append(char) }
            }
        }
    }
}

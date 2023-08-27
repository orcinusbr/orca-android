package com.jeanbarrossilva.orca.platform.ui.html.span.converter

import android.graphics.Typeface
import android.text.style.StyleSpan
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.html.span.SpanConverter

/** [SpanConverter] that converts a [StyleSpan] into a [SpanStyle]. **/
internal class StyleSpanConverter(override val next: SpanConverter?) : SpanConverter() {
    override fun onConvert(colors: Colors, span: Any): SpanStyle? {
        return if (span is StyleSpan) {
            SpanStyle(
                fontWeight = when (span.style) {
                    Typeface.BOLD, Typeface.BOLD_ITALIC -> FontWeight.Medium
                    else -> FontWeight.Normal
                },
                fontStyle = when (span.style) {
                    Typeface.BOLD_ITALIC, Typeface.ITALIC -> FontStyle.Italic
                    else -> FontStyle.Normal
                }
            )
        } else {
            null
        }
    }
}

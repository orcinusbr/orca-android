package com.jeanbarrossilva.orca.platform.ui.html.span.converter

import android.text.style.URLSpan
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import com.jeanbarrossilva.orca.platform.ui.html.span.SpanConverter

/** [SpanConverter] that converts an [URLSpan] into a [SpanStyle]. **/
internal class URLSpanConverter(override val next: SpanConverter?) : SpanConverter() {
    override fun onConvert(span: Any): SpanStyle? {
        return if (span is URLSpan) {
            SpanStyle(Color.Blue, textDecoration = TextDecoration.Underline)
        } else {
            null
        }
    }
}

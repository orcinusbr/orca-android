package com.jeanbarrossilva.mastodonte.platform.ui.html.span.converter

import android.text.style.UnderlineSpan
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import com.jeanbarrossilva.mastodonte.platform.ui.html.span.SpanConverter

/** [SpanConverter] that converts an [UnderlineSpan] into a [SpanStyle]. **/
internal class UnderlineSpanConverter(override val next: SpanConverter?) : SpanConverter() {
    override fun onConvert(span: Any): SpanStyle? {
        return if (span is UnderlineSpan) {
            SpanStyle(textDecoration = TextDecoration.Underline)
        } else {
            null
        }
    }
}

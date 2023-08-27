package com.jeanbarrossilva.orca.platform.ui.html.span.converter

import android.text.style.URLSpan
import androidx.compose.ui.text.SpanStyle
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.html.span.SpanConverter

/** [SpanConverter] that converts an [URLSpan] into a [SpanStyle]. **/
internal class URLSpanConverter(override val next: SpanConverter?) : SpanConverter() {
    override fun onConvert(colors: Colors, span: Any): SpanStyle? {
        return if (span is URLSpan) {
            SpanStyle(colors.brand.container)
        } else {
            null
        }
    }
}

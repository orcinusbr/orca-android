package com.jeanbarrossilva.mastodonte.platform.ui.html.span

import com.jeanbarrossilva.mastodonte.platform.ui.html.span.converter.StyleSpanConverter
import com.jeanbarrossilva.mastodonte.platform.ui.html.span.converter.UnderlineSpanConverter

/** Factory that creates a [SpanConverter] through [create]. **/
internal object SpanConverterFactory {
    /** Creates a [SpanConverter]. **/
    fun create(): SpanConverter {
        val styleSpanConverter = StyleSpanConverter(next = null)
        return UnderlineSpanConverter(next = styleSpanConverter)
    }
}

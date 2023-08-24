package com.jeanbarrossilva.orca.platform.ui.html.span

import com.jeanbarrossilva.orca.platform.ui.html.span.converter.StyleSpanConverter
import com.jeanbarrossilva.orca.platform.ui.html.span.converter.URLSpanConverter
import com.jeanbarrossilva.orca.platform.ui.html.span.converter.UnderlineSpanConverter

/** Factory that creates a [SpanConverter] through [create]. **/
internal object SpanConverterFactory {
    /** Creates a [SpanConverter]. **/
    fun create(): SpanConverter {
        val styleSpanConverter = StyleSpanConverter(next = null)
        val urlSpanConverter = URLSpanConverter(next = styleSpanConverter)
        return UnderlineSpanConverter(next = urlSpanConverter)
    }
}

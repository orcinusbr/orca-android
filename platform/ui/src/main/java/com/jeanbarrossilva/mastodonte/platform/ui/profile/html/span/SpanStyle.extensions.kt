package com.jeanbarrossilva.mastodonte.platform.ui.profile.html.span

import androidx.compose.ui.text.SpanStyle

/**
 * Creates a [SpanStyle] from the spans.
 *
 * @param spans Spans to create the [SpanStyle] with.
 **/
internal fun SpanStyle(spans: Array<out Any>): SpanStyle {
    var merged = SpanStyle()
    spans.forEach { merged += SpanConverterFactory.create().convert(it) }
    return merged
}

package com.jeanbarrossilva.orca.platform.ui.html.span

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors

/**
 * Creates a [SpanStyle] from the spans.
 *
 * @param colors [Colors] from which a [Color] by which the [SpanStyle] can be colored.
 * @param spans Spans to create the [SpanStyle] with.
 **/
internal fun SpanStyle(colors: Colors, spans: Array<out Any>): SpanStyle {
    var merged = SpanStyle()
    spans.forEach { merged += SpanConverterFactory.create().convert(colors, it) }
    return merged
}

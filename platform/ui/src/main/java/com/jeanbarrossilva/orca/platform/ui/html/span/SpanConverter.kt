package com.jeanbarrossilva.orca.platform.ui.html.span

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors

/** Handles the conversion of a span into a [SpanStyle]. **/
abstract class SpanConverter internal constructor() {
    /** [SpanConverter] through which the given span will be converted if this one can't do it. **/
    internal abstract val next: SpanConverter?

    /**
     * Converts the [span] into a [SpanStyle].
     *
     * @param colors [Colors] from which a [Color] by which the [SpanStyle] can be colored.
     * @param span Span to be converted.
     * @throws IllegalStateException If no [SpanConverter] is available for converting the given
     * [span].
     **/
    internal fun convert(colors: Colors, span: Any): SpanStyle {
        return onConvert(colors, span)
            ?: next?.convert(colors, span)
            ?: throw IllegalStateException("No converter found for $span.")
    }

    /**
     * Callback run when the [span] should be converted into a [SpanStyle].
     *
     * @param colors [Colors] from which a [Color] by which the [SpanStyle] can be colored.
     * @param span Span to be converted.
     **/
    protected abstract fun onConvert(colors: Colors, span: Any): SpanStyle?
}

package com.jeanbarrossilva.orca.platform.ui.html.span

import androidx.compose.ui.text.SpanStyle

/** Handles the conversion of a span into a [SpanStyle]. **/
internal abstract class SpanConverter internal constructor() {
    /**  **/
    abstract val next: SpanConverter?

    /**
     * Converts the [span] into a [SpanStyle].
     *
     * @param span Span to be converted.
     * @throws IllegalStateException If no [SpanConverter] is available for converting the given
     * [span].
     **/
    fun convert(span: Any): SpanStyle {
        return onConvert(span) ?: next?.convert(span) ?: throw IllegalStateException(
            "No converter found for $span."
        )
    }

    /**
     * Callback run when the [span] should be converted into a [SpanStyle].
     *
     * @param span Span to be converted.
     **/
    protected abstract fun onConvert(span: Any): SpanStyle?
}

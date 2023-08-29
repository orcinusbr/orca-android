package com.jeanbarrossilva.orca.platform.ui.html.span.converter

import android.text.style.URLSpan
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.html.span.SpanConverter

/** [SpanConverter] that converts an [URLSpan] into a [SpanStyle]. **/
class URLSpanConverter(override val next: SpanConverter?) : SpanConverter() {
    override fun onConvert(colors: Colors, span: Any): SpanStyle? {
        return if (span is URLSpan) {
            getSpanStyle(colors)
        } else {
            null
        }
    }

    companion object {
        /**
         * Gets the [SpanStyle] that's returned when the received span is a [URLSpan].
         *
         * @param colors [Colors] from which a [Color] by which the [SpanStyle] can be colored.
         **/
        fun getSpanStyle(colors: Colors): SpanStyle {
            return SpanStyle(colors.brand.container)
        }
    }
}

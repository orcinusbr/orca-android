package com.jeanbarrossilva.orca.platform.ui.core.style

import android.graphics.Typeface
import android.text.style.StyleSpan
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.StyledString

/**
 * Appends the [text] to the [StyledString] being built.
 *
 * @param text [Char] to be appended.
 * @param span Span to be applied to the [text].
 **/
internal fun StyledString.Builder.append(text: Char, span: Any) {
    when (span) {
        is StyleSpan -> append(text, span)
        else -> +text
    }
}

/**
 * Appends the [text] to the [StyledString] being built.
 *
 * @param text [Char] to be appended.
 * @param span [StyleSpan] to be applied to the [text].
 **/
private fun StyledString.Builder.append(text: Char, span: StyleSpan) {
    when (span.style) {
        Typeface.BOLD ->
            bold { +text }
        Typeface.BOLD_ITALIC ->
            bold {
                italic {
                    +text
                }
            }
        Typeface.ITALIC ->
            italic { +text }
    }
}

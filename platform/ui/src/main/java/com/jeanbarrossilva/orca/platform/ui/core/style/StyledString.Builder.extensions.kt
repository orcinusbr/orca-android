package com.jeanbarrossilva.orca.platform.ui.core.style

import android.graphics.Typeface
import android.text.style.StyleSpan
import com.jeanbarrossilva.orca.std.styledstring.StyledString

/**
 * Appends the [text] to the [StyledString] being built.
 *
 * @param span Span to be applied to the [text].
 * @param text [Char] to be appended.
 */
internal fun StyledString.Builder.append(span: Any, text: Char) {
  when (span) {
    is StyleSpan -> append(span, text)
    else -> +text
  }
}

/**
 * Appends the [text] to the [StyledString] being built.
 *
 * @param span [StyleSpan] to be applied to the [text].
 * @param text [Char] to be appended.
 */
private fun StyledString.Builder.append(span: StyleSpan, text: Char) {
  when (span.style) {
    Typeface.BOLD -> bold { +text }
    Typeface.BOLD_ITALIC -> bold { italic { +text } }
    Typeface.ITALIC -> italic { +text }
  }
}

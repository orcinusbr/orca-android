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
    is StyleSpan -> appendStyledChar(span, text)
    else -> +text
  }
}

/**
 * Appends the [char] to the [StyledString] being built.
 *
 * @param span [StyleSpan] that's been applied to the [char].
 * @param char [Char] to be appended.
 */
private fun StyledString.Builder.appendStyledChar(span: StyleSpan, char: Char) {
  when (span.style) {
    Typeface.BOLD -> bold { +char }
    Typeface.BOLD_ITALIC -> bold { italic { +char } }
    Typeface.ITALIC -> italic { +char }
  }
}

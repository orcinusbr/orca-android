package com.jeanbarrossilva.orca.platform.ui.core.style

import android.text.Spanned
import androidx.core.text.getSpans
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import com.jeanbarrossilva.orca.std.styledstring.buildStyledString

/** Converts this [Spanned] into a [StyledString]. */
fun Spanned.toStyledString(): StyledString {
  return buildStyledString {
    forEachIndexed { index, char ->
      getSpans<Any>(start = index, end = index).onEach { append(char, it) }.ifEmpty { +char }
    }
  }
}

package com.jeanbarrossilva.orca.platform.ui.core.style.spanned

import android.text.Spanned
import androidx.core.text.getSpans
import com.jeanbarrossilva.orca.platform.ui.core.style.append
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import com.jeanbarrossilva.orca.std.styledstring.buildStyledString

/** [Part]s by which this [Spanned] is composed. */
internal val Spanned.parts
  get() =
    mergeSpansIntoParts().fold(emptyList<Part>()) { accumulator, part ->
      if (accumulator.isNotEmpty() && part.indices.first > accumulator.last().indices.last.inc()) {
        accumulator + Part(accumulator.last().indices.last.inc()..part.indices.first.dec()) + part
      } else {
        accumulator + part
      }
    }

/** Converts this [Spanned] into a [StyledString]. */
fun Spanned.toStyledString(): StyledString {
  return buildStyledString {
    forEachIndexed { index, char ->
      getSpans<Any>(start = index, end = index).onEach { append(it, char) }.ifEmpty { +char }
    }
  }
}

/**
 * Merges all spans that have been applied to this [Spanned] into ordered spanned [Part]s, from
 * which the spans and also their respective indices can be obtained.
 *
 * @see Part.Spanned
 * @see Part.Spanned.getIndices
 */
private fun Spanned.mergeSpansIntoParts(): List<Part.Spanned> {
  return getSpans<Any>().map { Part(getSpanStart(it)..getSpanEnd(it).dec()).span(it) }
}

package com.jeanbarrossilva.orca.std.styledstring

/**
 * Returns whether this [Style] is chopped (that is, starts but overflows) by the [text].
 *
 * @param text [String] that may chop this [Style].
 */
internal fun Style.isChoppedBy(text: String): Boolean {
  return indices.first < text.length && indices.last > text.lastIndex
}

/**
 * Returns whether this [Style] is within the [text]'s bounds.
 *
 * @param text [String] in which this [Style] may be present.
 */
internal fun Style.isWithin(text: String): Boolean {
  return indices.first >= 0 && indices.last < text.length
}

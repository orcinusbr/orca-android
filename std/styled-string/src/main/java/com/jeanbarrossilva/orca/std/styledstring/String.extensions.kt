package com.jeanbarrossilva.orca.std.styledstring

/**
 * Applies [transform] to each portion of this [String] that matches the [regex].
 *
 * @param regex [Regex] of the portion to be transformed.
 * @param transform Transformation to be performed on the [regex]-matching [String].
 */
internal fun <T> String.map(
  regex: Regex,
  transform: (indices: IntRange, match: String) -> T
): List<T> {
  return regex.findAll(this).map { transform(it.range, it.value) }.toList()
}

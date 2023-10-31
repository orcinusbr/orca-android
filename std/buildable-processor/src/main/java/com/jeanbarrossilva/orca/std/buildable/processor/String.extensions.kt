package com.jeanbarrossilva.orca.std.buildable.processor

import java.util.Locale

/** Capitalizes this [String]. */
internal fun String.capitalize(): String {
  val locale = Locale.getDefault()
  return replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
}

/**
 * Replaces the last occurrence of the [replaced] by the [replacement].
 *
 * @param replaced [String] to be replaced.
 * @param replacement [String] to replace the [replaced].
 */
internal fun String.replaceLast(replaced: String, replacement: String): String {
  val lastIndex = lastIndexOf(replaced)
  val containsOldValue = lastIndex != -1
  return if (containsOldValue) replaceRange(lastIndex..lastIndex + replaced.lastIndex, replacement)
  else this
}

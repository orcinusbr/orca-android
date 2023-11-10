package com.jeanbarrossilva.orca.std.styledstring

/**
 * Applies the given [transform] to the currently iterated element if its [predicate] returns
 * `true`.
 *
 * @param I Element of the [Iterable] being conditionally mapped.
 * @param O Element of the resulting [List].
 * @param predicate Returns whether the element should be transformed.
 * @param transform Transformation to be performed on the element.
 */
internal fun <I, O : I> Iterable<I>.map(predicate: (I) -> Boolean, transform: (I) -> O): List<I> {
  return map { if (predicate(it)) transform(it) else it }
}

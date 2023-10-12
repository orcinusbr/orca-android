package com.jeanbarrossilva.orca.platform.ui.core.replacement

/** Creates an empty [ReplacementList]. */
internal fun <T> emptyReplacementList(): ReplacementList<T, T> {
  return ReplacementList(mutableListOf()) { it }
}

/**
 * Creates an empty [ReplacementList].
 *
 * @param selector Returns the value by which elements should be compared when replacing them.
 */
internal fun <I, O> emptyReplacementList(selector: (I) -> O): ReplacementList<I, O> {
  return ReplacementList(mutableListOf(), selector)
}

/**
 * Creates a [ReplacementList] with the given [elements].
 *
 * @param elements Elements to be added to the [ReplacementList].
 */
internal fun <T> replacementListOf(vararg elements: T): ReplacementList<T, T> {
  return ReplacementList(mutableListOf(*elements)) { it }
}

/**
 * Creates a [ReplacementList] with the given [elements].
 *
 * @param elements Elements to be added to the [ReplacementList].
 * @param selector Returns the value by which elements should be compared when replacing them.
 */
internal fun <I, O> replacementListOf(
  vararg elements: I,
  selector: (I) -> O
): ReplacementList<I, O> {
  return ReplacementList(mutableListOf(*elements), selector)
}

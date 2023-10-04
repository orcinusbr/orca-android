package com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable

/**
 * Returns a [List] containing exactly the same elements of this [Collection], except for the ones
 * that match the [predicate] and have been replaced by the result of [replacement].
 *
 * @param replacement Lambda that receives the candidate being currently iterated and returns the
 * replacement of the one that matches the [predicate].
 * @param predicate Indicates whether the given candidate should be replaced by the result of
 * [replacement].
 **/
fun <T> Collection<T>.replacingBy(replacement: T.() -> T, predicate: (T) -> Boolean): List<T> {
    return toMutableList().apply { replaceBy(replacement, predicate) }.toList()
}

/**
 * Returns a [List] containing exactly the same elements of this [Collection], except for the ones
 * that match the [predicate] and have been replaced by the result of [replacement].
 *
 * @param replacement Lambda that receives the candidate being currently iterated and returns the
 * replacement of the one that matches the [predicate].
 * @param predicate Indicates whether the given candidate should be replaced by the result of
 * [replacement].
 * @throws IllegalStateException If multiple elements match the [predicate].
 **/
internal fun <T> Collection<T>.replacingOnceBy(replacement: T.() -> T, predicate: (T) -> Boolean):
    List<T> {
    return toMutableList().apply { replaceOnceBy(replacement, predicate) }.toList()
}

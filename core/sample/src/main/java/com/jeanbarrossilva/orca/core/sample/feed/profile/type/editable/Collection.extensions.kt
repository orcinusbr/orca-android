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
    val iterator = iterator()
    val accumulator = mutableListOf<T>()
    while (iterator.hasNext()) {
        val candidate = iterator.next()
        val isReplaceable = predicate(candidate)
        val accumulation = if (isReplaceable) candidate.replacement() else candidate
        accumulator.add(accumulation)
    }
    return accumulator.toList()
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
    var replaced: T? = null
    return replacingBy(replacement) {
        val isMatch = predicate(it)
        if (isMatch && replaced != null) {
            throw IllegalStateException("Multiple predicate matches: $replaced and $it.")
        }
        if (isMatch) {
            replaced = it
        }
        isMatch
    }
}

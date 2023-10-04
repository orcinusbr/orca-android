package com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable

/**
 * Returns a [List] containing exactly the same elements of this [Collection], except for the ones
 * that match the [predicate] and have been replaced by the result of [replacement].
 *
 * @param replacement Lambda that receives the candidate being currently iterated and returns the
 * replacement of the one that matches the [predicate].
 * @param predicate Indicates whether the given candidate should be replaced by the result of
 * [replacement].
 * @return Whether an element matching the [predicate] has been found and replaced.
 * @throws IllegalStateException If multiple elements match the [predicate].
 **/
fun <T> MutableList<T>.replaceOnceBy(replacement: T.() -> T, predicate: (T) -> Boolean): Boolean {
    var replaced: T? = null
    replaceBy(replacement) {
        val isMatch = predicate(it)
        if (isMatch) {
            if (replaced == null) {
                replaced = it
            } else {
                throw IllegalStateException("Multiple predicate matches: $replaced and $it.")
            }
        }
        isMatch
    }
    return replaced != null
}

/**
 * Replaces the elements that match the [predicate] by the result of [replacement].
 *
 * @param replacement Lambda that receives the candidate being currently iterated and returns the
 * replacement of the one that matches the [predicate].
 * @param predicate Indicates whether the given candidate should be replaced by the result of
 * [replacement].
 **/
internal fun <T> MutableList<T>.replaceBy(replacement: T.() -> T, predicate: (T) -> Boolean) {
    replaceAll {
        if (predicate(it)) it.replacement() else it
    }
}

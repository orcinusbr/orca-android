package com.jeanbarrossilva.orca.std.injector

/**
 * Casts this to [T].
 *
 * @param T Value to which this will be casted.
 * @throws ClassCastException If this cannot be casted to [T].
 */
@PublishedApi
@Throws(ClassCastException::class)
internal fun <T> Any.castTo(): T {
  @Suppress("UNCHECKED_CAST") return this as T
}

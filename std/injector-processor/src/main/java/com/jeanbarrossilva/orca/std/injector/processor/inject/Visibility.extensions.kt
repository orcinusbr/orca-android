package com.jeanbarrossilva.orca.std.injector.processor.inject

import com.google.devtools.ksp.symbol.Visibility

/**
 * Returns the [Visibility] that's least visible.
 *
 * @param a [Visibility] to be compared to [b].
 * @param b [Visibility] to be compared to [a].
 **/
internal fun minOf(a: Visibility, b: Visibility): Visibility {
    val aWeight = a.weight()
    val bWeight = b.weight()
    val minWeight = listOf(aWeight, bWeight).min()
    return if (minWeight == aWeight) a else b
}

/**
 * Since the [Visibility] enum entries aren't sorted according to their "weight", returns a positive
 * [Int] that classifies this [Visibility].
 **/
private fun Visibility.weight(): Int {
    return when (this) {
        Visibility.LOCAL -> 0
        Visibility.PRIVATE -> 1
        Visibility.PROTECTED -> 2
        Visibility.JAVA_PACKAGE -> 3
        Visibility.INTERNAL -> 4
        Visibility.PUBLIC -> 5
    }
}

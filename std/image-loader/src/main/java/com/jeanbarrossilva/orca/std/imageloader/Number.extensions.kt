package com.jeanbarrossilva.orca.std.imageloader

/**
 * Returns [max] if this [Int] is less than [min] or [min] if this is greater than [max]; otherwise,
 * this [Int] itself is returned.
 *
 * @param min Minimum [Int] to be returned if this one exceeds [max].
 * @param max Maximum [Int] to be returned if this one is less than [min].
 **/
internal fun Int.mirror(min: Int, max: Int): Int {
    return if (this < min) max else if (this > max) min else this
}

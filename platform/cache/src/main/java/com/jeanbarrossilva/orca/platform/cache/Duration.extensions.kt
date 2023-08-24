package com.jeanbarrossilva.orca.platform.cache

import kotlin.time.Duration

/**
 * Returns how much time has passed since this in relation to [other].
 *
 * This method is simply an alias for [minus] with the intent of improving readability.
 *
 * @param other [Duration] at the end of the range.
 **/
internal infix fun Duration.since(other: Duration): Duration {
    return minus(other)
}

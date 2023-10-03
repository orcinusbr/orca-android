package com.jeanbarrossilva.orca.core.feed.profile.toot.stat.toggleable

import kotlinx.coroutines.flow.flowOf

/** Creates an empty [ToggleableStat]. **/
fun <T> emptyToggleableStat(): ToggleableStat<T> {
    return buildToggleableStat(count = 0) {
        get {
            flowOf(emptyList())
        }
    }
}

/**
 * Builds a [ToggleableStat].
 *
 * @param count Initial amount of elements of the [ToggleableStat].
 * @param build Configuration for the [ToggleableStat].
 **/
fun <T> buildToggleableStat(count: Int, build: ToggleableStat.Builder<T>.() -> Unit):
    ToggleableStat<T> {
    return ToggleableStat.Builder<T>(count).apply(build).build()
}

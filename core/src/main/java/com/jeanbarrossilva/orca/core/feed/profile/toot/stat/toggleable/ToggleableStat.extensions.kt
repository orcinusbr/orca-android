package com.jeanbarrossilva.orca.core.feed.profile.toot.stat.toggleable

import com.jeanbarrossilva.orca.core.feed.profile.toot.stat.Stat
import kotlinx.coroutines.flow.flowOf

/** [Stat] that can have its enable-ability toggled. */
fun <T> ToggleableStat(): ToggleableStat<T> {
  return ToggleableStat(count = 0) { get { flowOf(emptyList()) } }
}

/**
 * [Stat] that can have its enable-ability toggled.
 *
 * @param count Initial amount of elements of the [ToggleableStat].
 * @param build Configuration for the [ToggleableStat].
 */
fun <T> ToggleableStat(count: Int, build: ToggleableStat.Builder<T>.() -> Unit): ToggleableStat<T> {
  return ToggleableStat.Builder<T>(count).apply(build).build()
}

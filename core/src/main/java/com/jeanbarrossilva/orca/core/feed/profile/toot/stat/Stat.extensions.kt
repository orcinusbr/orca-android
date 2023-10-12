package com.jeanbarrossilva.orca.core.feed.profile.toot.stat

import kotlinx.coroutines.flow.flowOf

/**
 * Specific statistic whose [count] doesn't necessarily reflect the summed [size][List.size] of all
 * [List]s emitted to the result of [get]. Although all core variants should be as precise as
 * possible when defining what the total amount of elements counted by this [Stat] is, there is no
 * precise and efficient way of guaranteeing parity.
 */
fun <T> Stat(): Stat<T> {
  return Stat(count = 0) { get { flowOf(emptyList()) } }
}

/**
 * Specific statistic whose [count] doesn't necessarily reflect the summed [size][List.size] of all
 * [List]s emitted to the result of [get]. Although all core variants should be as precise as
 * possible when defining what the total amount of elements counted by this [Stat] is, there is no
 * precise and efficient way of guaranteeing parity.
 *
 * @param count Initial amount of elements of the [Stat].
 * @param build Configuration for the [Stat].
 */
fun <T> Stat(count: Int, build: Stat.Builder<T>.() -> Unit): Stat<T> {
  return Stat.Builder<T>(count).apply(build).build()
}

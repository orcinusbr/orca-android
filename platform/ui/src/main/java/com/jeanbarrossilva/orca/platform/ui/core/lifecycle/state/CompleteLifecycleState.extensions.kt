package com.jeanbarrossilva.orca.platform.ui.core.lifecycle.state

/**
 * Compares this [CompleteLifecycleState] with the given [other]; if it is `null`, then it is
 * considered to be less than [other].
 *
 * @param other [CompleteLifecycleState] to compare this one with.
 **/
infix operator fun CompleteLifecycleState?.compareTo(other: CompleteLifecycleState): Int {
    return this?.compareTo(other) ?: -1
}

/**
 * Whether this [CompleteLifecycleState] equals to or is greater than the given [state].
 *
 * @param state [CompleteLifecycleState] to compare this one with.
 **/
fun CompleteLifecycleState?.isAtLeast(state: CompleteLifecycleState): Boolean {
    return this != null && compareTo(state) >= 0
}

/**
 * Provides the [CompleteLifecycleState] that succeeds this one; if it's `null`, provides the
 * [created][CompleteLifecycleState.CREATED] [CompleteLifecycleState].
 **/
internal fun CompleteLifecycleState?.next(): CompleteLifecycleState? {
    return if (this == null) CompleteLifecycleState.CREATED else next()
}

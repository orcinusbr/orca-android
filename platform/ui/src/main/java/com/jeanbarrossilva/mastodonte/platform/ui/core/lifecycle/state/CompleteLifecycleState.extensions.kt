package com.jeanbarrossilva.mastodonte.platform.ui.core.lifecycle.state

/**
 * Whether this [CompleteLifecycleState] equals to or is greater than the given [state].
 *
 * @param state [CompleteLifecycleState] to compare this one with.
 **/
fun CompleteLifecycleState?.isAtLeast(state: CompleteLifecycleState): Boolean {
    return this != null && compareTo(state) >= 0
}

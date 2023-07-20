package com.jeanbarrossilva.mastodonte.feature.auth.activity

/** Whether this [AuthActivity.LifecycleState] equals to or is greater than the given [state]. **/
internal fun AuthActivity.LifecycleState?.isAtLeast(state: AuthActivity.LifecycleState): Boolean {
    return this != null && compareTo(state) >= 0
}

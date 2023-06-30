package com.jeanbarrossilva.mastodonte.platform.ui.core

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit

/**
 * Navigates to the given [destination] by adding it to the container.
 *
 * @param containerID ID of the [Fragment] container.
 * @param isAnimated Whether the transition is animated.
 * @param destination Callback that provides the [Fragment] that'll get navigated to.
 **/
fun FragmentManager.navigate(
    @IdRes containerID: Int,
    isAnimated: Boolean = true,
    destination: () -> Fragment
) {
    commit {
        addToBackStack(null)
        if (isAnimated) {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }
        add(containerID, destination())
    }
}

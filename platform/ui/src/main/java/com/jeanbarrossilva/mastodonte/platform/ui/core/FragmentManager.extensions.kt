package com.jeanbarrossilva.mastodonte.platform.ui.core

import androidx.annotation.IdRes
import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit

/**
 * Denotes that an [Int] value should refer to one of [FragmentTransaction]'s transition styles.
 *
 * @see FragmentTransaction.TRANSIT_NONE
 * @see FragmentTransaction.TRANSIT_FRAGMENT_OPEN
 * @see FragmentTransaction.TRANSIT_FRAGMENT_CLOSE
 * @see FragmentTransaction.TRANSIT_FRAGMENT_FADE
 **/
@IntDef(
    FragmentTransaction.TRANSIT_NONE,
    FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
    FragmentTransaction.TRANSIT_FRAGMENT_CLOSE,
    FragmentTransaction.TRANSIT_FRAGMENT_FADE
)
@Retention(AnnotationRetention.SOURCE)
private annotation class Transit

/**
 * Navigates to the given [destination] by adding it to the container.
 *
 * @param containerID ID of the [Fragment] container.
 * @param transition Indicates how the transition will be animated.
 * @param destination Callback that provides the [Fragment] that'll get navigated to.
 **/
fun FragmentManager.navigate(
    @IdRes containerID: Int,
    @Transit transition: Int = FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
    destination: () -> Fragment
) {
    commit {
        addToBackStack(null)
        setTransition(transition)
        add(containerID, destination())
    }
}

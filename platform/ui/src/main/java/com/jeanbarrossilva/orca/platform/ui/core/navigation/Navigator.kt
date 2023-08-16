package com.jeanbarrossilva.orca.platform.ui.core.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.jeanbarrossilva.orca.platform.ui.core.navigation.duplication.Duplication
import com.jeanbarrossilva.orca.platform.ui.core.navigation.duplication.allowingDuplication
import com.jeanbarrossilva.orca.platform.ui.core.navigation.duplication.disallowingDuplication
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.Transition

/**
 * Navigates to [Fragment]s through [navigate].
 *
 * @param fragmentManager [FragmentManager] that adds [Fragment]s to the [FragmentContainerView].
 * @param containerID ID of the [FragmentContainerView] to which [Fragment]s will be added.
 **/
class Navigator internal constructor(
    private val fragmentManager: FragmentManager,
    @IdRes private val containerID: Int
) {
    /**
     * Defines the [Destination.Provider] that will provide the [Fragment] destination through [to].
     **/
    class Navigation<T : Fragment> internal constructor() {
        /**
         * Target navigation site.
         *
         * @param T [Fragment] to be navigated to.
         * @param route Path by which the [fragment] can be retrieved.
         * @param fragment [Fragment] to be navigated to.
         **/
        class Destination<T : Fragment>(val route: String, val fragment: T) {
            /**
             * Provides the [Destination] to navigate to through [provide]. Can be instantiated via
             * [to].
             *
             * @param T [Fragment] to navigate to.
             **/
            abstract class Provider<T : Fragment> internal constructor() {
                /** Provides the [Destination] to navigate to. **/
                internal abstract fun provide(): Destination<T>
            }
        }

        /**
         * Creates a [Destination.Provider].
         *
         * @param route Path by which the result of [target] can be retrieved.
         * @param target Returns the [Fragment] to navigate to.
         **/
        fun to(route: String, target: () -> T): Destination.Provider<T> {
            return object : Destination.Provider<T>() {
                override fun provide(): Destination<T> {
                    return Destination(route, target())
                }
            }
        }
    }

    /**
     * Navigates to the [Fragment] destination provided by the result of [navigation].
     *
     * @param T [Fragment] to navigate to.
     * @param transition [Transition] with which the navigation will be animated.
     * @param duplication Indicates whether the navigation should be performed even if the current
     * [Navigation.Destination]'s and that of the provided by the result of [navigation] are the
     * same.
     * @param navigation Defines where to navigate to.
     * @see allowingDuplication
     * @see disallowingDuplication
     * @see Navigation.to
     **/
    fun <T : Fragment> navigate(
        transition: Transition,
        duplication: Duplication = allowingDuplication(),
        navigation: Navigation<T>.() -> Navigation.Destination.Provider<T>
    ) {
        val previousRoute = fragmentManager.fragments.lastOrNull()?.tag
        val destination = Navigation<T>().navigation().provide()
        val canNavigate = duplication.canNavigate(previousRoute, currentRoute = destination.route)
        if (canNavigate) {
            unconditionallyNavigate(transition, destination)
        }
    }

    /** Pops the back stack. **/
    fun pop() {
        fragmentManager.popBackStack()
    }

    /**
     * Navigates to the [destination] without checking if we're allowed to do so.
     *
     * @param transition [Transition] with which the navigation will be animated.
     * @param destination [Navigation.Destination] to navigate to.
     **/
    private fun unconditionallyNavigate(
        transition: Transition,
        destination: Navigation.Destination<*>
    ) {
        fragmentManager.commit {
            addToBackStack(null)
            setTransition(transition.value)
            add(containerID, destination.fragment, destination.route)
        }
        fragmentManager.executePendingTransactions()
    }
}

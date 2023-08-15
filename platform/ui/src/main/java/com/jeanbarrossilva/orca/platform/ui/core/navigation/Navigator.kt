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
import kotlin.reflect.KClass

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
     * Defines the [DestinationProvider] that will provide the [Fragment] destination through [to].
     **/
    class Navigation<T : Fragment> private constructor() {
        /**
         * Provides the [Fragment] destination to navigate to through [provide]. Can be instantiated
         * via [to].
         *
         * @param T [Fragment] to navigate to.
         **/
        abstract class DestinationProvider<T : Fragment> internal constructor() {
            /** Provides the [Fragment] destination to navigate to. **/
            internal abstract fun provide(): T
        }

        /**
         * Creates a [DestinationProvider] that provides the result of the given [provision].
         *
         * @param provision Returns the [Fragment] destination to navigate to.
         **/
        fun to(provision: () -> T): DestinationProvider<T> {
            return object : DestinationProvider<T>() {
                override fun provide(): T {
                    return provision()
                }
            }
        }

        companion object {
            /** Preexistent [Navigation] instances. **/
            private val instances = hashMapOf<KClass<out Fragment>, Navigation<out Fragment>>()

            /**
             * Gets an existent instance of [Navigation] for the given [fragmentClass] or creates a
             * new one and returns it.
             *
             * @param T [Fragment] for which the [Navigation] is.
             * @param fragmentClass [KClass] of the [Fragment].
             **/
            internal fun <T : Fragment> `for`(fragmentClass: KClass<T>): Navigation<T> {
                @Suppress("UNCHECKED_CAST")
                return instances.getOrPut(fragmentClass) { Navigation<T>() } as Navigation<T>
            }
        }
    }

    /**
     * Navigates to the [Fragment] destination provided by the result of [navigation].
     *
     * @param T [Fragment] to navigate to.
     * @param transition [Transition] with which the navigation will be animated.
     * @param duplication Indicates whether the navigation should be performed even if the
     * current [Fragment]'s and the destination's types are the same.
     * @param navigation Defines where to navigate to.
     * @see allowingDuplication
     * @see disallowingDuplication
     * @see Navigation.to
     **/
    inline fun <reified T : Fragment> navigate(
        transition: Transition,
        duplication: Duplication = allowingDuplication(),
        noinline navigation: Navigation<T>.() -> Navigation.DestinationProvider<T>
    ) {
        navigate(T::class, transition, duplication, navigation)
    }

    /** Pops the back stack. **/
    fun pop() {
        fragmentManager.popBackStack()
    }

    /**
     * Navigates to the [Fragment] destination provided by the result of [navigation].
     *
     * @param T [Fragment] to navigate to.
     * @param fragmentClass [KClass] of the [Fragment].
     * @param transition [Transition] with which the navigation will be animated.
     * @param duplication Indicates whether the navigation should be performed even if the
     * current [Fragment]'s and the destination's types are the same.
     * @param navigation Defines where to navigate to.
     * @see Navigation.to
     **/
    @PublishedApi
    internal fun <T : Fragment> navigate(
        fragmentClass: KClass<T>,
        transition: Transition,
        duplication: Duplication,
        navigation: Navigation<T>.() -> Navigation.DestinationProvider<T>
    ) {
        val currentFragment = fragmentManager.fragments.lastOrNull()
        val canNavigate = duplication.canNavigateTo(fragmentClass, currentFragment)
        if (canNavigate) {
            unconditionallyNavigate(fragmentClass, transition, navigation)
        }
    }

    /**
     * Navigates to the [Fragment] destination provided by the result of [provision] without
     * checking if we're allowed to do so.
     *
     * @param T [Fragment] to navigate to.
     * @param fragmentClass [KClass] of the [Fragment].
     * @param transition [Transition] with which the navigation will be animated.
     * @param provision Defines where to navigate to.
     * @see Navigation.to
     **/
    private fun <T : Fragment> unconditionallyNavigate(
        fragmentClass: KClass<T>,
        transition: Transition,
        provision: Navigation<T>.() -> Navigation.DestinationProvider<T>
    ) {
        fragmentManager.commit {
            val destination = Navigation.`for`(fragmentClass).provision().provide()
            addToBackStack(null)
            setTransition(transition.value)
            add(containerID, destination, destination.tag ?: tagFor(fragmentClass))
        }
        fragmentManager.executePendingTransactions()
    }

    companion object {
        /**
         * Creates a tag for the given [Fragment].
         *
         * @param T [Fragment] for which a tag will be created.
         **/
        inline fun <reified T : Fragment> tagFor(): String {
            return tagFor(T::class)
        }

        /**
         * Creates a tag for a [Fragment] whose [KClass] is equal to the given [fragmentClass].
         *
         * @param T [Fragment] for which a tag will be created.
         * @param fragmentClass [KClass] of the [Fragment].
         **/
        @PublishedApi
        internal fun <T : Fragment> tagFor(fragmentClass: KClass<T>): String {
            return fragmentClass.simpleName ?: throw IllegalStateException(
                "Could not create a tag for $fragmentClass."
            )
        }
    }
}

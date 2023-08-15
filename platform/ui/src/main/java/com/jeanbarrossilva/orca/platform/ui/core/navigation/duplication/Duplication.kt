package com.jeanbarrossilva.orca.platform.ui.core.navigation.duplication

import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

/** Indicates the approval or lack thereof of duplicate navigation. **/
sealed class Duplication {
    /** Indicates that duplicate navigation is disallowed. **/
    internal object Disallowed : Duplication() {
        override fun <T : Fragment> canNavigateTo(
            fragmentClass: KClass<T>,
            currentFragment: Fragment?
        ): Boolean {
            return currentFragment == null || currentFragment::class != fragmentClass
        }
    }

    /** Indicates that duplicate navigation is allowed. **/
    internal object Allowed : Duplication() {
        override fun <T : Fragment> canNavigateTo(
            fragmentClass: KClass<T>,
            currentFragment: Fragment?
        ): Boolean {
            return true
        }
    }

    /**
     * Determines whether navigation to a [Fragment] whose [KClass] equals to the given
     * [fragmentClass] can be performed.
     *
     * @param T [Fragment] to which navigation has been requested to be performed.
     * @param fragmentClass [KClass] of the [Fragment].
     * @param currentFragment [Fragment] that's the current one.
     **/
    internal abstract fun <T : Fragment> canNavigateTo(
        fragmentClass: KClass<T>,
        currentFragment: Fragment?
    ): Boolean
}

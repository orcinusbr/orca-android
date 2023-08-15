package com.jeanbarrossilva.orca.platform.ui.core.navigation.duplication

import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator

/** Indicates the approval or lack thereof of duplicate navigation. **/
sealed class Duplication {
    /** Indicates that duplicate navigation is disallowed. **/
    internal object Disallowed : Duplication() {
        override fun canNavigate(
            previous: Navigator.Navigation<*>?,
            current: Navigator.Navigation<*>
        ): Boolean {
            return previous !== current
        }
    }

    /** Indicates that duplicate navigation is allowed. **/
    internal object Allowed : Duplication() {
        override fun canNavigate(
            previous: Navigator.Navigation<*>?,
            current: Navigator.Navigation<*>
        ): Boolean {
            return true
        }
    }

    /**
     * Determines whether navigation from [previous] to [current] can be performed.
     *
     * @param previous Metadata of the previously scheduled or performed navigation.
     * @param current Metadata of the navigation that has been requested to be performed.
     **/
    internal abstract fun canNavigate(
        previous: Navigator.Navigation<*>?,
        current: Navigator.Navigation<*>
    ): Boolean
}

package com.jeanbarrossilva.orca.platform.ui.core.navigation.duplication

import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator

/** Indicates the approval or lack thereof of duplicate navigation. **/
sealed class Duplication {
    /** Indicates that duplicate navigation is disallowed. **/
    internal object Disallowed : Duplication() {
        override fun canNavigate(previousRoute: String?, currentRoute: String): Boolean {
            return previousRoute == null || currentRoute != previousRoute
        }
    }

    /** Indicates that duplicate navigation is allowed. **/
    internal object Allowed : Duplication() {
        override fun canNavigate(previousRoute: String?, currentRoute: String): Boolean {
            return true
        }
    }

    /**
     * Determines whether navigation can be performed.
     *
     * @param previousRoute Route of the previous [Navigator.Navigation.Destination].
     * @param currentRoute Route to which navigation has been requested.
     **/
    internal abstract fun canNavigate(previousRoute: String?, currentRoute: String): Boolean
}

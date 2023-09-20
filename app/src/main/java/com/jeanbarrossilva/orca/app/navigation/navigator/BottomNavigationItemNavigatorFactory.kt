package com.jeanbarrossilva.orca.app.navigation.navigator

import com.jeanbarrossilva.orca.app.navigation.navigator.provider.FeedBottomNavigationItemNavigator
import com.jeanbarrossilva.orca.app.navigation.navigator.provider.ProfileBottomNavigationItemNavigator
import com.jeanbarrossilva.orca.core.auth.AuthenticationLock

internal object BottomNavigationItemNavigatorFactory {
    fun create(authenticationLock: AuthenticationLock): BottomNavigationItemNavigator {
        val feedNavigator = FeedBottomNavigationItemNavigator(next = null)
        return ProfileBottomNavigationItemNavigator(authenticationLock, next = feedNavigator)
    }
}

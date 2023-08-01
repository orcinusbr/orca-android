package com.jeanbarrossilva.orca.app.navigation.navigator

import com.jeanbarrossilva.orca.app.navigation.navigator.provider.FeedBottomNavigationItemNavigator
import com.jeanbarrossilva.orca.app.navigation.navigator.provider.ProfileBottomNavigationItemNavigator

internal object BottomNavigationItemNavigatorFactory {
    fun create(): BottomNavigationItemNavigator {
        val feedNavigator = FeedBottomNavigationItemNavigator(next = null)
        return ProfileBottomNavigationItemNavigator(next = feedNavigator)
    }
}

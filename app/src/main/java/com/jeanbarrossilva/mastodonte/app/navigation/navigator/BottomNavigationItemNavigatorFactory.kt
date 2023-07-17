package com.jeanbarrossilva.mastodonte.app.navigation.navigator

import com.jeanbarrossilva.mastodonte.app.navigation.navigator.provider.ProfileBottomNavigationItemNavigator

internal object BottomNavigationItemNavigatorFactory {
    fun create(): BottomNavigationItemNavigator {
        return ProfileBottomNavigationItemNavigator(next = null)
    }
}

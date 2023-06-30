package com.jeanbarrossilva.mastodonte.app.navigation

import com.jeanbarrossilva.mastodonte.app.navigation.provider.ProfileBottomNavigationItemNavigator

internal object BottomNavigationItemNavigatorFactory {
    fun create(): BottomNavigationItemNavigator {
        return ProfileBottomNavigationItemNavigator(next = null)
    }
}

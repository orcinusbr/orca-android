package com.jeanbarrossilva.orca.app.navigation.navigator

import com.jeanbarrossilva.orca.app.navigation.navigator.provider.FeedBottomNavigationItemNavigator
import com.jeanbarrossilva.orca.app.navigation.navigator.provider.ProfileBottomNavigationItemNavigator
import com.jeanbarrossilva.orca.core.auth.actor.Actor

internal object BottomNavigationItemNavigatorFactory {
    fun create(actor: Actor.Authenticated): BottomNavigationItemNavigator {
        val feedNavigator = FeedBottomNavigationItemNavigator(next = null)
        return ProfileBottomNavigationItemNavigator(actor, next = feedNavigator)
    }
}

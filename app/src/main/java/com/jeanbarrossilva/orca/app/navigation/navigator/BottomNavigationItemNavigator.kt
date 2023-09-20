package com.jeanbarrossilva.orca.app.navigation.navigator

import androidx.annotation.IdRes
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.platform.ui.core.navigation.duplication.disallowingDuplication
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.suddenly

abstract class BottomNavigationItemNavigator {
    protected abstract val next: BottomNavigationItemNavigator?

    suspend fun navigate(navigator: Navigator, @IdRes itemID: Int) {
        val destination = getDestination(itemID)
            ?: next?.getDestination(itemID)
            ?: throw IllegalStateException("No destination found for item $itemID.")
        navigator.navigate(suddenly(), disallowingDuplication()) {
            to(destination.route, destination.target)
        }
    }

    protected abstract suspend fun getDestination(@IdRes itemID: Int):
        Navigator.Navigation.Destination<*>?
}

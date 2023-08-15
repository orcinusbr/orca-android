package com.jeanbarrossilva.orca.app.navigation.navigator

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.suddenly

abstract class BottomNavigationItemNavigator {
    protected abstract val next: BottomNavigationItemNavigator?

    fun navigate(navigator: Navigator, @IdRes itemID: Int) {
        navigator.navigate(suddenly()) {
            to {
                getFragment(itemID)
                    ?: next?.getFragment(itemID)
                    ?: throw IllegalArgumentException("No provider for item identified as $itemID.")
            }
        }
    }

    protected abstract fun getFragment(@IdRes itemID: Int): Fragment?
}

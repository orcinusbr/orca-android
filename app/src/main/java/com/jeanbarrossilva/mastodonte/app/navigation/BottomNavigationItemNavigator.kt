package com.jeanbarrossilva.mastodonte.app.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

abstract class BottomNavigationItemNavigator {
    protected abstract val next: BottomNavigationItemNavigator?

    fun navigate(fragmentManager: FragmentManager, @IdRes containerID: Int, @IdRes itemID: Int) {
        val fragment = getFragment(itemID)
            ?: next?.getFragment(itemID)
            ?: throw IllegalArgumentException("No provider for item identified as $itemID.")
        fragmentManager.commit {
            add(containerID, fragment)
        }
    }

    protected abstract fun getFragment(@IdRes itemID: Int): Fragment?
}

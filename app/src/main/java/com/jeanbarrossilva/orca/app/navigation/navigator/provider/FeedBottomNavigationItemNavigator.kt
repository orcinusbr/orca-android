package com.jeanbarrossilva.orca.app.navigation.navigator.provider

import androidx.fragment.app.Fragment
import com.jeanbarrossilva.orca.app.R
import com.jeanbarrossilva.orca.app.navigation.navigator.BottomNavigationItemNavigator
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import com.jeanbarrossilva.orca.feature.feed.FeedFragment

internal class FeedBottomNavigationItemNavigator(
    override val next: BottomNavigationItemNavigator?
) : BottomNavigationItemNavigator() {
    override fun getFragment(itemID: Int): Fragment? {
        return if (itemID == R.id.feed) FeedFragment(Profile.sample.id) else null
    }
}

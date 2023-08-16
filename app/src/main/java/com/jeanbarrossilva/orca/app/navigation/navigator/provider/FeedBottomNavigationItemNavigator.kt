package com.jeanbarrossilva.orca.app.navigation.navigator.provider

import com.jeanbarrossilva.orca.app.R
import com.jeanbarrossilva.orca.app.navigation.navigator.BottomNavigationItemNavigator
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import com.jeanbarrossilva.orca.feature.feed.FeedFragment
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator

internal class FeedBottomNavigationItemNavigator(
    override val next: BottomNavigationItemNavigator?
) : BottomNavigationItemNavigator() {
    override fun getDestination(itemID: Int): Navigator.Navigation.Destination<FeedFragment>? {
        return if (itemID == R.id.feed) {
            Navigator.Navigation.Destination("feed") {
                FeedFragment(Profile.sample.id)
            }
        } else {
            null
        }
    }
}

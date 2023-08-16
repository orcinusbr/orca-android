package com.jeanbarrossilva.orca.app.navigation.navigator.provider

import com.jeanbarrossilva.orca.app.R
import com.jeanbarrossilva.orca.app.navigation.navigator.BottomNavigationItemNavigator
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsFragment
import com.jeanbarrossilva.orca.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator

internal class ProfileBottomNavigationItemNavigator(
    override val next: BottomNavigationItemNavigator?
) : BottomNavigationItemNavigator() {
    private val id = Profile.sample.id

    override fun getDestination(itemID: Int):
        Navigator.Navigation.Destination<ProfileDetailsFragment>? {
        return if (itemID == R.id.profile_details) {
            Navigator.Navigation.Destination(ProfileDetailsFragment.createRoute(id)) {
                ProfileDetailsFragment(BackwardsNavigationState.Unavailable, id)
            }
        } else {
            null
        }
    }
}

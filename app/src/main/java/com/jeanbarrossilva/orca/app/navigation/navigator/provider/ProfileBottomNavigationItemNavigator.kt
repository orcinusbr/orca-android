package com.jeanbarrossilva.orca.app.navigation.navigator.provider

import com.jeanbarrossilva.orca.app.R
import com.jeanbarrossilva.orca.app.navigation.navigator.BottomNavigationItemNavigator
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsFragment
import com.jeanbarrossilva.orca.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator

internal class ProfileBottomNavigationItemNavigator(
    private val actor: Actor.Authenticated,
    override val next: BottomNavigationItemNavigator?
) : BottomNavigationItemNavigator() {
    override suspend fun getDestination(itemID: Int):
        Navigator.Navigation.Destination<ProfileDetailsFragment>? {
        return if (itemID == R.id.profile_details) {
            Navigator.Navigation.Destination(ProfileDetailsFragment.createRoute(actor.id)) {
                ProfileDetailsFragment(BackwardsNavigationState.Unavailable, actor.id)
            }
        } else {
            null
        }
    }
}

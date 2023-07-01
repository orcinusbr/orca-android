package com.jeanbarrossilva.mastodonte.app.navigation.provider

import androidx.fragment.app.Fragment
import com.jeanbarrossilva.mastodon.feature.profiledetails.ProfileDetailsFragment
import com.jeanbarrossilva.mastodon.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.mastodonte.app.R
import com.jeanbarrossilva.mastodonte.app.navigation.BottomNavigationItemNavigator
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.sample.profile.sample

internal class ProfileBottomNavigationItemNavigator(
    override val next: BottomNavigationItemNavigator?
) : BottomNavigationItemNavigator() {
    override val tag = ProfileDetailsFragment.TAG

    override fun getFragment(itemID: Int): Fragment? {
        return if (itemID == R.id.profile_details) {
            ProfileDetailsFragment(BackwardsNavigationState.Unavailable, Profile.sample.id)
        } else {
            null
        }
    }
}

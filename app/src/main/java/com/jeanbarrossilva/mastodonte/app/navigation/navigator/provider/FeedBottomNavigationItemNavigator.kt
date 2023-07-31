package com.jeanbarrossilva.mastodonte.app.navigation.navigator.provider

import androidx.fragment.app.Fragment
import com.jeanbarrossilva.mastodonte.app.R
import com.jeanbarrossilva.mastodonte.app.navigation.navigator.BottomNavigationItemNavigator
import com.jeanbarrossilva.mastodonte.core.feed.profile.Profile
import com.jeanbarrossilva.mastodonte.core.sample.feed.profile.sample
import com.jeanbarrossilva.mastodonte.feature.feed.FeedFragment

internal class FeedBottomNavigationItemNavigator(
    override val next: BottomNavigationItemNavigator?
) : BottomNavigationItemNavigator() {
    override val tag = FeedFragment.TAG

    override fun getFragment(itemID: Int): Fragment? {
        return if (itemID == R.id.feed) FeedFragment(Profile.sample.id) else null
    }
}

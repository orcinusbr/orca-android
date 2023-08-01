package com.jeanbarrossilva.orca.feature.profiledetails

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.orca.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.platform.ui.core.argument
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.platform.ui.core.context.ContextProvider
import org.koin.android.ext.android.inject

class ProfileDetailsFragment internal constructor() : ComposableFragment(), ContextProvider {
    private val profileProvider by inject<ProfileProvider>()
    private val tootProvider by inject<TootProvider>()
    private val id by argument<String>(ID_KEY)
    private val viewModel by viewModels<ProfileDetailsViewModel> {
        ProfileDetailsViewModel
            .createFactory(contextProvider = this, profileProvider, tootProvider, id)
    }
    private val navigator by inject<ProfileDetailsBoundary>()
    private val onBottomAreaAvailabilityChangeListener by
        inject<OnBottomAreaAvailabilityChangeListener>()

    constructor(backwardsNavigationState: BackwardsNavigationState, id: String) : this() {
        arguments =
            bundleOf(BACKWARDS_NAVIGATION_STATE_KEY to backwardsNavigationState, ID_KEY to id)
    }

    @Composable
    override fun Content() {
        val backwardsNavigationState by remember {
            argument<BackwardsNavigationState>(BACKWARDS_NAVIGATION_STATE_KEY)
        }

        ProfileDetails(
            viewModel,
            navigator,
            backwardsNavigationState,
            onBottomAreaAvailabilityChangeListener
        )
    }

    override fun provide(): Context {
        return requireContext()
    }

    companion object {
        internal const val ID_KEY = "id"
        internal const val BACKWARDS_NAVIGATION_STATE_KEY = "backwards-navigation-state"

        const val TAG = "profile-details-fragment"
    }
}

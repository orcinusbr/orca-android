package com.jeanbarrossilva.mastodon.feature.profiledetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.mastodon.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.mastodon.feature.profiledetails.viewmodel.ProfileDetailsViewModel
import com.jeanbarrossilva.mastodonte.core.profile.ProfileRepository
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.mastodonte.platform.ui.core.application
import com.jeanbarrossilva.mastodonte.platform.ui.core.argument
import com.jeanbarrossilva.mastodonte.platform.ui.core.composable.ComposableFragment
import org.koin.android.ext.android.inject

class ProfileDetailsFragment private constructor() : ComposableFragment() {
    private val repository by inject<ProfileRepository>()
    private val id by argument<String>(ID_KEY)
    private val viewModel by viewModels<ProfileDetailsViewModel> {
        ProfileDetailsViewModel.createFactory(application, repository, id)
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

    companion object {
        private const val ID_KEY = "id"
        private const val BACKWARDS_NAVIGATION_STATE_KEY = "backwards-navigation-state"

        const val TAG = "profile-details-fragment"
    }
}

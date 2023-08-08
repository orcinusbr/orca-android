package com.jeanbarrossilva.orca.feature.profiledetails

import android.content.Context
import androidx.annotation.IdRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.orca.platform.ui.core.argument
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.platform.ui.core.context.ContextProvider
import com.jeanbarrossilva.orca.platform.ui.core.navigate
import org.koin.android.ext.android.get
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
            onBottomAreaAvailabilityChangeListener = get()
        )
    }

    override fun provide(): Context {
        return requireContext()
    }

    companion object {
        internal const val ID_KEY = "id"
        internal const val BACKWARDS_NAVIGATION_STATE_KEY = "backwards-navigation-state"

        const val TAG = "profile-details-fragment"

        fun navigate(fragmentManager: FragmentManager, @IdRes containerID: Int, id: String) {
            fragmentManager.navigate(containerID, TAG) {
                val backwardsNavigationState =
                    BackwardsNavigationState.Available.createInstance(fragmentManager::popBackStack)
                ProfileDetailsFragment(backwardsNavigationState, id)
            }
        }
    }
}

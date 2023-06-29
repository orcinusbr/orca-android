package com.jeanbarrossilva.mastodon.feature.profiledetails

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeanbarrossilva.mastodon.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.mastodon.feature.profiledetails.viewmodel.ProfileDetailsViewModel
import com.jeanbarrossilva.mastodonte.core.profile.ProfileRepository
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.mastodonte.platform.ui.composable.ComposableFragment
import org.koin.android.ext.android.inject

internal class ProfileDetailsFragment : ComposableFragment() {
    private val navArgs by navArgs<ProfileDetailsFragmentArgs>()
    private val repository by inject<ProfileRepository>()
    private val viewModel by viewModels<ProfileDetailsViewModel> {
        ProfileDetailsViewModel.createFactory(requireActivity().application, repository, navArgs.id)
    }
    private val navigator by inject<ProfileDetailsNavigator>()
    private val onBottomAreaAvailabilityChangeListener by
        inject<OnBottomAreaAvailabilityChangeListener>()

    @Composable
    override fun Content() {
        val navController = findNavController()
        val backwardsNavigationState = BackwardsNavigationState.from(navController)
        ProfileDetails(
            viewModel,
            navigator,
            backwardsNavigationState,
            onBottomAreaAvailabilityChangeListener
        )
    }
}

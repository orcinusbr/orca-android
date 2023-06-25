package com.jeanbarrossilva.mastodonte.app.destination

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jeanbarrossilva.mastodon.feature.profile.Profile
import com.jeanbarrossilva.mastodon.feature.profile.navigation.BackwardsNavigationState
import com.jeanbarrossilva.mastodon.feature.profile.viewmodel.ProfileViewModel
import com.jeanbarrossilva.mastodonte.core.inmemory.profile.sample
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.ProfileRepository
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import org.koin.compose.koinInject

@Composable
@Destination
@RootNavGraph(start = true)
internal fun Profile(
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener,
    modifier: Modifier = Modifier
) {
    val repository = koinInject<ProfileRepository>()
    val viewModelFactory = ProfileViewModel.createFactory(repository, Profile.sample.id)
    val viewModel = viewModel<ProfileViewModel>(factory = viewModelFactory)

    Profile(
        viewModel,
        onNavigationToTootDetails = { },
        onEdit = { },
        BackwardsNavigationState.Unavailable,
        onBottomAreaAvailabilityChangeListener,
        modifier
    )
}

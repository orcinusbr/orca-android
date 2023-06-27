package com.jeanbarrossilva.mastodonte.app.feature.profile

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jeanbarrossilva.mastodon.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.mastodon.feature.profiledetails.ProfileDetailsNavigator
import com.jeanbarrossilva.mastodon.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.mastodon.feature.profiledetails.viewmodel.ProfileDetailsViewModel
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
internal fun ProfileDetails(
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener,
    modifier: Modifier = Modifier
) {
    val application = koinInject<Application>()
    val repository = koinInject<ProfileRepository>()
    val viewModelFactory =
        ProfileDetailsViewModel.createFactory(application, repository, Profile.sample.id)
    val viewModel = viewModel<ProfileDetailsViewModel>(factory = viewModelFactory)
    val navigator = koinInject<ProfileDetailsNavigator>()

    ProfileDetails(
        viewModel,
        navigator,
        BackwardsNavigationState.Unavailable,
        onBottomAreaAvailabilityChangeListener,
        modifier
    )
}

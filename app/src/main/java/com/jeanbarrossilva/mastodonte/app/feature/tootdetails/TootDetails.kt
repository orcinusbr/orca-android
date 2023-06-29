package com.jeanbarrossilva.mastodonte.app.feature.tootdetails

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jeanbarrossilva.mastodonte.core.profile.toot.TootRepository
import com.jeanbarrossilva.mastodonte.feature.tootdetails.TootDetails
import com.jeanbarrossilva.mastodonte.feature.tootdetails.TootDetailsNavigator
import com.jeanbarrossilva.mastodonte.feature.tootdetails.viewmodel.TootDetailsViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import org.koin.compose.koinInject

@Composable
@Destination
@RootNavGraph(start = true)
internal fun TootDetails(id: String = "", modifier: Modifier = Modifier) {
    val application = koinInject<Application>()
    val repository = koinInject<TootRepository>()
    val viewModelFactory = TootDetailsViewModel.createFactory(application, repository, id)
    val viewModel = viewModel<TootDetailsViewModel>(factory = viewModelFactory)
    val navigator = koinInject<TootDetailsNavigator>()

    TootDetails(viewModel, navigator, modifier)
}

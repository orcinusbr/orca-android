package com.jeanbarrossilva.mastodonte.feature.tootdetails

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.jeanbarrossilva.mastodonte.core.profile.toot.TootRepository
import com.jeanbarrossilva.mastodonte.feature.tootdetails.viewmodel.TootDetailsViewModel
import com.jeanbarrossilva.mastodonte.platform.ui.composable.ComposableFragment
import org.koin.android.ext.android.inject

internal class TootDetailsFragment : ComposableFragment() {
    private val navArgs by navArgs<TootDetailsFragmentArgs>()
    private val repository by inject<TootRepository>()
    private val viewModel by viewModels<TootDetailsViewModel> {
        TootDetailsViewModel.createFactory(requireActivity().application, repository, navArgs.id)
    }
    private val navigator by inject<TootDetailsNavigator>()

    @Composable
    override fun Content() {
        TootDetails(viewModel, navigator)
    }
}

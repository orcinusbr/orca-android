package com.jeanbarrossilva.mastodonte.feature.tootdetails

import androidx.annotation.IdRes
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.mastodonte.core.profile.toot.TootRepository
import com.jeanbarrossilva.mastodonte.feature.tootdetails.viewmodel.TootDetailsViewModel
import com.jeanbarrossilva.mastodonte.platform.ui.core.application
import com.jeanbarrossilva.mastodonte.platform.ui.core.argument
import com.jeanbarrossilva.mastodonte.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.mastodonte.platform.ui.core.navigate
import org.koin.android.ext.android.inject

class TootDetailsFragment private constructor() : ComposableFragment() {
    private val repository by inject<TootRepository>()
    private val id by argument<String>(ID_KEY)
    private val viewModel by viewModels<TootDetailsViewModel> {
        TootDetailsViewModel.createFactory(application, repository, id)
    }
    private val navigator by inject<TootDetailsBoundary>()

    private constructor(id: String) : this() {
        arguments = bundleOf(ID_KEY to id)
    }

    @Composable
    override fun Content() {
        TootDetails(viewModel, navigator)
    }

    companion object {
        private const val ID_KEY = "id"

        fun navigate(
            fragmentManager: FragmentManager,
            @IdRes containerID: Int,
            id: String
        ) {
            fragmentManager.navigate(containerID) {
                TootDetailsFragment(id)
            }
        }
    }
}

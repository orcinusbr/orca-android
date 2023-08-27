package com.jeanbarrossilva.orca.feature.tootdetails

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.feature.tootdetails.viewmodel.TootDetailsViewModel
import com.jeanbarrossilva.orca.platform.ui.core.argument
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.opening
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class TootDetailsFragment private constructor() : ComposableFragment() {
    private val provider by inject<TootProvider>()
    private val id by argument<String>(ID_KEY)
    private val viewModel by viewModels<TootDetailsViewModel> {
        TootDetailsViewModel.createFactory(contextProvider = ::requireContext, provider, id)
    }
    private val navigator by inject<TootDetailsBoundary>()

    private constructor(id: String) : this() {
        arguments = bundleOf(ID_KEY to id)
    }

    @Composable
    override fun Content() {
        TootDetails(viewModel, navigator, onBottomAreaAvailabilityChangeListener = get())
    }

    companion object {
        private const val ID_KEY = "id"

        fun getRoute(id: String): String {
            return "toot/$id"
        }

        fun navigate(navigator: Navigator, id: String) {
            navigator.navigate(opening()) {
                to(getRoute(id)) {
                    TootDetailsFragment(id)
                }
            }
        }
    }
}

package com.jeanbarrossilva.orca.feature.tootdetails

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.core.instance.SomeInstance
import com.jeanbarrossilva.orca.feature.tootdetails.viewmodel.TootDetailsViewModel
import com.jeanbarrossilva.orca.platform.ui.core.argument
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.opening
import org.koin.android.ext.android.get

class TootDetailsFragment private constructor() : ComposableFragment() {
    private val id by argument<String>(ID_KEY)
    private val viewModel by viewModels<TootDetailsViewModel> {
        TootDetailsViewModel
            .createFactory(contextProvider = ::requireContext, get<SomeInstance>().tootProvider, id)
    }

    private constructor(id: String) : this() {
        arguments = bundleOf(ID_KEY to id)
    }

    @Composable
    override fun Content() {
        TootDetails(viewModel, boundary = get(), onBottomAreaAvailabilityChangeListener = get())
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

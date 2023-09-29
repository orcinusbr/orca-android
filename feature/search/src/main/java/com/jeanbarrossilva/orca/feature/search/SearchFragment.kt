package com.jeanbarrossilva.orca.feature.search

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.platform.ui.core.injected
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.opening
import com.jeanbarrossilva.orca.std.injector.Injector

class SearchFragment : ComposableFragment() {
    private val viewModel by viewModels<SearchViewModel> {
        SearchViewModel.createFactory(Instance.injected.profileSearcher)
    }

    @Composable
    override fun Content() {
        Search(viewModel, boundary = Injector.get())
    }

    companion object {
        fun navigate(navigator: Navigator) {
            navigator.navigate(opening()) {
                to("search", ::SearchFragment)
            }
        }
    }
}

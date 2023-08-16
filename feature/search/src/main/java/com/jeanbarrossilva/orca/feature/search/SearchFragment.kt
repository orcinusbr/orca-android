package com.jeanbarrossilva.orca.feature.search

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearcher
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.opening
import org.koin.android.ext.android.inject

class SearchFragment : ComposableFragment() {
    private val searcher by inject<ProfileSearcher>()
    private val viewModel by viewModels<SearchViewModel> { SearchViewModel.createFactory(searcher) }
    private val boundary by inject<SearchBoundary>()

    @Composable
    override fun Content() {
        Search(viewModel, boundary)
    }

    companion object {
        fun navigate(navigator: Navigator) {
            navigator.navigate(opening()) {
                to("search", ::SearchFragment)
            }
        }
    }
}

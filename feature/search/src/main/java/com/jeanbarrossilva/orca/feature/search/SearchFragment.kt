package com.jeanbarrossilva.orca.feature.search

import androidx.annotation.IdRes
import androidx.compose.runtime.Composable
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearcher
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.platform.ui.core.navigate
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
        fun navigate(fragmentManager: FragmentManager, @IdRes containerID: Int) {
            fragmentManager.navigate(containerID, "search-fragment", destination = ::SearchFragment)
        }
    }
}

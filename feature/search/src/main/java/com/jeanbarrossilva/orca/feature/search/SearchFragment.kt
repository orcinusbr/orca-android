package com.jeanbarrossilva.orca.feature.search

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.opening
import com.jeanbarrossilva.orca.std.injector.Injector

class SearchFragment : ComposableFragment() {
  private val module by lazy { Injector.from<SearchModule>() }
  private val viewModel by
    viewModels<SearchViewModel> { SearchViewModel.createFactory(module.searcher()) }

  @Composable
  override fun Content() {
    Search(viewModel, module.boundary())
  }

  companion object {
    fun navigate(navigator: Navigator) {
      navigator.navigate(opening()) { to("search", ::SearchFragment) }
    }
  }
}

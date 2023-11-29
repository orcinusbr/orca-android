package com.jeanbarrossilva.orca.feature.postdetails

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.feature.postdetails.viewmodel.PostDetailsViewModel
import com.jeanbarrossilva.orca.platform.ui.core.argument
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.opening
import com.jeanbarrossilva.orca.std.injector.Injector

class PostDetailsFragment private constructor() : ComposableFragment() {
  private val module by lazy { Injector.from<PostDetailsModule>() }
  private val id by argument<String>(ID_KEY)
  private val viewModel by
    viewModels<PostDetailsViewModel> {
      PostDetailsViewModel.createFactory(
        contextProvider = ::requireContext,
        module.postProvider(),
        id
      )
    }

  private constructor(id: String) : this() {
    arguments = bundleOf(ID_KEY to id)
  }

  @Composable
  override fun Content() {
    PostDetails(viewModel, module.boundary(), module.onBottomAreaAvailabilityChangeListener())
  }

  companion object {
    private const val ID_KEY = "id"

    fun getRoute(id: String): String {
      return "post/$id"
    }

    fun navigate(navigator: Navigator, id: String) {
      navigator.navigate(opening()) { to(getRoute(id)) { PostDetailsFragment(id) } }
    }
  }
}

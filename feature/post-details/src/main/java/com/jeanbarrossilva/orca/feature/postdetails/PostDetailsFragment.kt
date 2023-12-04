/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

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
        id,
        onLinkClick = module.boundary()::navigateTo
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

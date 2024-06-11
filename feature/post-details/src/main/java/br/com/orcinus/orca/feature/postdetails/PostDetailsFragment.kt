/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.feature.postdetails

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import br.com.orcinus.orca.composite.composable.ComposableFragment
import br.com.orcinus.orca.feature.postdetails.viewmodel.PostDetailsViewModel
import br.com.orcinus.orca.platform.navigation.Navigator
import br.com.orcinus.orca.platform.navigation.application
import br.com.orcinus.orca.platform.navigation.argument
import br.com.orcinus.orca.platform.navigation.parentNavigator
import br.com.orcinus.orca.platform.navigation.transition.opening
import br.com.orcinus.orca.std.injector.Injector

class PostDetailsFragment private constructor() : ComposableFragment() {
  private val module by lazy { Injector.from<PostDetailsModule>() }
  private val id by argument<String>(ID_KEY)
  private val viewModel by
    viewModels<PostDetailsViewModel> {
      PostDetailsViewModel.createFactory(
        application,
        module.postProvider(),
        id,
        onLinkClick = boundary::navigateTo,
        onThumbnailClickListener = boundary::navigateToGallery
      )
    }

  private val boundary
    get() = module.boundary()

  private constructor(id: String) : this() {
    arguments = bundleOf(ID_KEY to id)
  }

  @Composable
  override fun Content() {
    PostDetails(
      viewModel,
      onNavigateToDetails = { boundary.navigateToPostDetails(parentNavigator, it) },
      onBackwardsNavigation = { boundary.pop(parentNavigator) }
    )
  }

  companion object {
    private const val ID_KEY = "id"

    fun navigate(navigator: Navigator, id: String) {
      navigator.navigate(opening()) { PostDetailsFragment(id) }
    }
  }
}

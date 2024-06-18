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

package br.com.orcinus.orca.feature.feed

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import br.com.orcinus.orca.composite.composable.ComposableFragment
import br.com.orcinus.orca.platform.navigation.BackStack
import br.com.orcinus.orca.platform.navigation.Navigator
import br.com.orcinus.orca.platform.navigation.application
import br.com.orcinus.orca.platform.navigation.argument
import br.com.orcinus.orca.std.injector.Injector

class FeedFragment internal constructor() : ComposableFragment() {
  private val module by lazy { Injector.from<FeedModule>() }
  private val backStack by BackStack.from(this)
  private val userID by argument<String>(USER_ID_KEY)
  private val navigator by lazy { Navigator.create(this, backStack) }
  private val viewModel by
    viewModels<FeedViewModel> {
      FeedViewModel.createFactory(
        application,
        module.feedProvider(),
        module.postProvider(),
        userID,
        onLinkClick = boundary::navigateTo,
        onThumbnailClickListener = { postID, entrypointIndex, secondary, entrypoint ->
          module.boundary().navigateToGallery(postID, entrypointIndex, secondary, entrypoint)
        }
      )
    }

  private val boundary
    get() = module.boundary()

  constructor(backStack: BackStack, userID: String) : this() {
    arguments = bundleOf(BackStack.KEY to backStack, USER_ID_KEY to userID)
  }

  @Composable
  override fun Content() {
    Feed(
      viewModel,
      onSearch = { boundary.navigateToSearch(navigator) },
      onPostClick = { boundary.navigateToPostDetails(navigator, backStack, it) },
      onComposition = { boundary.navigateToComposer() }
    )
  }

  companion object {
    private const val USER_ID_KEY = "user-id"
  }
}

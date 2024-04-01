/*
 * Copyright © 2023-2024 Orcinus
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

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import br.com.orcinus.orca.composite.composable.ComposableFragment
import br.com.orcinus.orca.platform.navigation.application
import br.com.orcinus.orca.platform.navigation.argument
import br.com.orcinus.orca.std.injector.Injector

class FeedFragment internal constructor() : ComposableFragment() {
  private val module by lazy { Injector.from<FeedModule>() }
  private val userID by argument<String>(USER_ID_KEY)
  private val viewModel by
    viewModels<FeedViewModel> {
      FeedViewModel.createFactory(
        application,
        module.feedProvider(),
        module.postProvider(),
        userID,
        onLinkClick = module.boundary()::navigateTo,
        onThumbnailClickListener = { postID, entrypointIndex, secondary, entrypoint ->
          module.boundary().navigateToGallery(postID, entrypointIndex, secondary, entrypoint)
        }
      )
    }

  constructor(userID: String) : this() {
    arguments = createArguments(userID)
  }

  @Composable
  override fun Content() {
    Feed(viewModel, module.boundary())
  }

  companion object {
    internal const val USER_ID_KEY = "user-id"

    const val ROUTE = "feed"

    internal fun createArguments(userID: String): Bundle {
      return bundleOf(USER_ID_KEY to userID)
    }
  }
}

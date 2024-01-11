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

package com.jeanbarrossilva.orca.feature.profiledetails

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.orca.platform.navigation.Navigator
import com.jeanbarrossilva.orca.platform.navigation.transition.opening
import com.jeanbarrossilva.orca.platform.ui.core.argument
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.platform.ui.core.context.ContextProvider
import com.jeanbarrossilva.orca.std.injector.Injector

class ProfileDetailsFragment internal constructor() : ComposableFragment(), ContextProvider {
  private val module by lazy { Injector.from<ProfileDetailsModule>() }
  private val id by argument<String>(ID_KEY)
  private val viewModel by
    viewModels<ProfileDetailsViewModel> {
      ProfileDetailsViewModel.createFactory(
        contextProvider = this,
        module.profileProvider(),
        module.postProvider(),
        id,
        onLinkClick = module.boundary()::navigateTo,
        onThumbnailClickListener = module.boundary()::navigateToGallery
      )
    }

  constructor(backwardsNavigationState: BackwardsNavigationState, id: String) : this() {
    arguments = bundleOf(BACKWARDS_NAVIGATION_STATE_KEY to backwardsNavigationState, ID_KEY to id)
  }

  @Composable
  override fun Content() {
    val backwardsNavigationState by remember {
      argument<BackwardsNavigationState>(BACKWARDS_NAVIGATION_STATE_KEY)
    }

    ProfileDetails(
      viewModel,
      navigator = module.get(),
      backwardsNavigationState,
      onBottomAreaAvailabilityChangeListener = module.get()
    )
  }

  override fun provide(): Context {
    return requireContext()
  }

  companion object {
    internal const val ID_KEY = "id"
    internal const val BACKWARDS_NAVIGATION_STATE_KEY = "backwards-navigation-state"

    fun createRoute(id: String): String {
      return "profile/$id"
    }

    fun navigate(navigator: Navigator, id: String) {
      navigator.navigate(opening()) {
        to(createRoute(id)) {
          ProfileDetailsFragment(
            BackwardsNavigationState.Available.createInstance(navigator::pop),
            id
          )
        }
      }
    }
  }
}

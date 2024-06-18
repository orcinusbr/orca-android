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

package br.com.orcinus.orca.feature.profiledetails

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import br.com.orcinus.orca.composite.composable.ComposableFragment
import br.com.orcinus.orca.feature.profiledetails.navigation.BackwardsNavigationState
import br.com.orcinus.orca.platform.navigation.BackStack
import br.com.orcinus.orca.platform.navigation.Navigator
import br.com.orcinus.orca.platform.navigation.application
import br.com.orcinus.orca.platform.navigation.argument
import br.com.orcinus.orca.platform.navigation.transition.opening
import br.com.orcinus.orca.std.injector.Injector

class ProfileDetailsFragment internal constructor() : ComposableFragment() {
  private val module by lazy { Injector.from<ProfileDetailsModule>() }
  private val backStack by BackStack.from(this)
  private val backwardsNavigationState by
    argument<BackwardsNavigationState>(BACKWARDS_NAVIGATION_STATE_KEY)
  private val id by argument<String>(ID_KEY)
  private val navigator by lazy { Navigator.create(this, backStack) }
  private val viewModel by
    viewModels<ProfileDetailsViewModel> {
      ProfileDetailsViewModel.createFactory(
        application,
        module.profileProvider(),
        module.postProvider(),
        id,
        onLinkClick = boundary::navigateTo,
        onThumbnailClickListener = module.boundary()::navigateToGallery
      )
    }

  private val boundary
    get() = module.boundary()

  constructor(
    backStack: BackStack,
    backwardsNavigationState: BackwardsNavigationState,
    id: String
  ) : this() {
    arguments =
      bundleOf(
        BackStack.KEY to backStack,
        BACKWARDS_NAVIGATION_STATE_KEY to backwardsNavigationState,
        ID_KEY to id
      )
  }

  @Composable
  override fun Content() {
    ProfileDetails(
      viewModel,
      boundary,
      backwardsNavigationState,
      onNavigationToPostDetails = { boundary.navigateToPostDetails(navigator, backStack, it) }
    )
  }

  companion object {
    internal const val ID_KEY = "id"
    internal const val BACKWARDS_NAVIGATION_STATE_KEY = "backwards-navigation-state"

    fun navigate(navigator: Navigator, backStack: BackStack, id: String) {
      navigator.navigate(opening()) {
        ProfileDetailsFragment(
          backStack,
          BackwardsNavigationState.Available.createInstance(navigator::pop),
          id
        )
      }
    }
  }
}

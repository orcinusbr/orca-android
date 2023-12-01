/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.feature.profiledetails

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.orca.platform.ui.core.argument
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.platform.ui.core.context.ContextProvider
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.opening
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
        id
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

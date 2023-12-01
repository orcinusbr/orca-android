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

package com.jeanbarrossilva.orca.feature.feed

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.platform.ui.core.argument
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.platform.ui.core.context.ContextProvider
import com.jeanbarrossilva.orca.std.injector.Injector

class FeedFragment internal constructor() : ComposableFragment(), ContextProvider {
  private val module by lazy { Injector.from<FeedModule>() }
  private val userID by argument<String>(USER_ID_KEY)
  private val viewModel by
    viewModels<FeedViewModel> {
      FeedViewModel.createFactory(
        contextProvider = this,
        module.feedProvider(),
        module.postProvider(),
        userID
      )
    }

  constructor(userID: String) : this() {
    arguments = bundleOf(USER_ID_KEY to userID)
  }

  @Composable
  override fun Content() {
    Feed(viewModel, boundary = module.get(), onBottomAreaAvailabilityChangeListener = module.get())
  }

  override fun provide(): Context {
    return requireContext()
  }

  companion object {
    internal const val USER_ID_KEY = "user-id"
  }
}

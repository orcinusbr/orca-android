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

package com.jeanbarrossilva.orca.feature.composer

import android.content.Context
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableActivity
import com.jeanbarrossilva.orca.platform.ui.core.on

class ComposerActivity internal constructor() : ComposableActivity() {
  private val viewModel by viewModels<ComposerViewModel>()

  @Composable
  override fun Content() {
    Composer(viewModel, onBackwardsNavigation = ::finish)
  }

  companion object {
    fun start(context: Context) {
      context.on<ComposerActivity>().asNewTask().start()
    }
  }
}

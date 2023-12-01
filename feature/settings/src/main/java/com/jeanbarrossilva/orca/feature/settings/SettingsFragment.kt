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

package com.jeanbarrossilva.orca.feature.settings

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.std.injector.Injector

class SettingsFragment : ComposableFragment() {
  private val module by lazy { Injector.from<SettingsModule>() }
  private val viewModel by
    viewModels<SettingsViewModel> { SettingsViewModel.createFactory(module.termMuter()) }

  @Composable
  override fun Content() {
    Settings(viewModel, module.boundary())
  }
}

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

package com.jeanbarrossilva.orca.feature.settings

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.composite.composable.ComposableFragment
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

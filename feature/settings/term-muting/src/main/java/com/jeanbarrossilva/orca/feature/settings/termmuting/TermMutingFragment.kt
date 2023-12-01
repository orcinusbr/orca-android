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

package com.jeanbarrossilva.orca.feature.settings.termmuting

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.opening
import com.jeanbarrossilva.orca.std.injector.Injector

class TermMutingFragment internal constructor() : ComposableFragment() {
  private val module by lazy { Injector.from<TermMutingModule>() }
  private val viewModel by
    viewModels<TermMutingViewModel> { TermMutingViewModel.createFactory(module.termMuter()) }

  @Composable
  override fun Content() {
    TermMuting(viewModel, module.boundary())
  }

  companion object {
    fun navigate(navigator: Navigator) {
      navigator.navigate(opening()) { to("settings/term-muting", ::TermMutingFragment) }
    }
  }
}

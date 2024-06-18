/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.feature.registration

import androidx.compose.runtime.Composable
import br.com.orcinus.orca.composite.composable.ComposableFragment
import br.com.orcinus.orca.platform.navigation.Navigator
import br.com.orcinus.orca.platform.navigation.transition.opening
import br.com.orcinus.orca.std.injector.Injector

class RegistrationFragment internal constructor() : ComposableFragment() {
  private val module by lazy { Injector.from<RegistrationModule>() }

  @Composable
  override fun Content() {
    Registration(module.boundary())
  }

  companion object {
    fun navigate(navigator: Navigator) {
      navigator.navigate(opening(), ::RegistrationFragment)
    }
  }
}

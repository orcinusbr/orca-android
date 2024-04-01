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

package br.com.orcinus.orca.feature.registration.credentials

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import br.com.orcinus.orca.composite.composable.ComposableFragment
import br.com.orcinus.orca.platform.navigation.Navigator
import br.com.orcinus.orca.platform.navigation.transition.opening

class CredentialsFragment internal constructor() : ComposableFragment() {
  private val viewModel by
    viewModels<CredentialsViewModel>(factoryProducer = CredentialsViewModel::createFactory)

  @Composable
  override fun Content() {
    Credentials(viewModel)
  }

  companion object {
    internal const val ROUTE = "credentials"

    fun navigate(navigator: Navigator) {
      navigator.navigate(opening()) { to(ROUTE, ::CredentialsFragment) }
    }
  }
}

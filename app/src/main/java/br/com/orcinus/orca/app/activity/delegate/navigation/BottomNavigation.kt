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

package br.com.orcinus.orca.app.activity.delegate.navigation

import android.view.MenuItem
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import br.com.orcinus.orca.app.R
import br.com.orcinus.orca.app.activity.delegate.Binding
import br.com.orcinus.orca.platform.navigation.BackStack
import br.com.orcinus.orca.platform.navigation.Navigator
import kotlinx.coroutines.launch

internal interface BottomNavigation : Binding {
  fun navigateOnItemSelection(activity: FragmentActivity) {
    binding?.bottomNavigationView?.setOnItemSelectedListener {
      navigate(activity, it)
      true
    }
  }

  fun selectDefaultItem() {
    binding?.bottomNavigationView?.selectedItemId = R.id.feed
  }

  private fun navigate(activity: FragmentActivity, item: MenuItem) {
    val backStack = BackStack.named(item.title?.toString() ?: return)
    val navigator = Navigator.create(activity, backStack)
    activity.lifecycleScope.launch {
      BottomNavigationFragmentProvider.navigate(navigator, backStack, item.itemId)
    }
  }
}

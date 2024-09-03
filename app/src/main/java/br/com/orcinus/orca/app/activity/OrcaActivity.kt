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

package br.com.orcinus.orca.app.activity

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import br.com.orcinus.orca.app.OrcaApplication
import br.com.orcinus.orca.app.R
import br.com.orcinus.orca.app.databinding.ActivityOrcaBinding
import br.com.orcinus.orca.platform.navigation.BackStack
import br.com.orcinus.orca.platform.navigation.Navigator
import br.com.orcinus.orca.std.injector.Injector
import kotlinx.coroutines.launch

/**
 * Main [Activity] that gets started when the app launches.
 *
 * It is primarily intended for configuring state which either cannot be set up by the
 * [OrcaApplication] because it requires a pre-lifecycle-ending deconfiguration (in this case, such
 * "teardown" should be performed in [onDestroy]) or is directly intrinsic to the [Window].
 *
 * When it is destroyed, all dependencies that were previously injected are dejected by default.
 * This behavior can be modified by overriding [areDependenciesDejectedOnDestruction].
 *
 * @see getWindow
 * @see onDestroy
 */
internal open class OrcaActivity : FragmentActivity() {
  /**
   * [ActivityOrcaBinding] containing references to the [View]s specified in the layout XML file.
   * Gets assigned a non-null value on creation and is nullified after destruction.
   */
  private var binding: ActivityOrcaBinding? = null

  /** Whether all injected dependencies should be dejected when [onDestroy] is finally called. */
  protected open val areDependenciesDejectedOnDestruction = true

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    binding = ActivityOrcaBinding.inflate(layoutInflater)
    setContentView(binding?.root)
    navigateOnBottomNavigationItemSelection()
    selectDefaultBottomNavigationItem()
  }

  override fun onDestroy() {
    super.onDestroy()
    binding = null
    if (areDependenciesDejectedOnDestruction) {
      Injector.clear()
    }
  }

  /**
   * Listens to selections on each of the bottom navigation [MenuItem]s and navigates to their
   * respective [Fragment] when they are performed. Each [MenuItem] is considered to be selected
   * unconditionally afterwards.
   */
  private fun navigateOnBottomNavigationItemSelection() {
    binding?.bottomNavigationView?.setOnItemSelectedListener {
      navigate(it)
      true
    }
  }

  /**
   * Navigates to the [Fragment] associated to the [item].
   *
   * @param item [MenuItem] that is considered to have been selected and to whose associated
   *   [Fragment] navigation will be performed.
   */
  private fun navigate(item: MenuItem) {
    val itemTitle = item.title?.toString() ?: return
    val backStack = BackStack.named(itemTitle)
    val navigator = Navigator.create(this, backStack)
    lifecycleScope.launch {
      BottomNavigationFragmentProvider.navigate(navigator, backStack, item.itemId)
    }
  }

  /** Selects the bottom navigation [MenuItem] that is considered to be the default one. */
  private fun selectDefaultBottomNavigationItem() {
    binding?.bottomNavigationView?.selectedItemId = R.id.feed
  }
}

/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.orca.app.activity

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowCompat
import com.jeanbarrossilva.orca.app.activity.delegate.BottomNavigation
import com.jeanbarrossilva.orca.app.activity.delegate.BottomNavigationViewAvailability
import com.jeanbarrossilva.orca.app.activity.delegate.Injection
import com.jeanbarrossilva.orca.app.databinding.ActivityOrcaBinding
import com.jeanbarrossilva.orca.app.module.core.MainMastodonCoreModule
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.platform.autos.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.platform.ui.core.navigation.NavigationActivity

open class OrcaActivity :
  NavigationActivity(),
  OnBottomAreaAvailabilityChangeListener,
  Injection,
  BottomNavigationViewAvailability,
  BottomNavigation {
  protected open val coreModule: CoreModule = MainMastodonCoreModule

  override var binding: ActivityOrcaBinding? = null
  override var constraintSet: ConstraintSet? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    bindView(this)
    updateBottomAreaAvailability(this)
    setContentView(binding?.root)
    inject(this, coreModule)
    navigateOnItemSelection(this)
    navigateToDefaultDestination()
  }
}

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

import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import br.com.orcinus.orca.app.activity.delegate.Injection
import br.com.orcinus.orca.app.activity.delegate.navigation.BottomNavigation
import br.com.orcinus.orca.app.databinding.ActivityOrcaBinding
import br.com.orcinus.orca.app.module.core.MainMastodonCoreModule
import br.com.orcinus.orca.core.module.CoreModule

internal open class OrcaActivity : FragmentActivity(), Injection, BottomNavigation {
  protected open val coreModule: CoreModule = MainMastodonCoreModule

  override var binding: ActivityOrcaBinding? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    bindView(this)
    setContentView(binding?.root)
    inject(this, coreModule)
    navigateOnItemSelection(this)
    selectDefaultItem()
  }
}

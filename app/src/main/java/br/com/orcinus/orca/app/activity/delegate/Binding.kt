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

package br.com.orcinus.orca.app.activity.delegate

import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import br.com.orcinus.orca.app.databinding.ActivityOrcaBinding

internal interface Binding {
  var binding: ActivityOrcaBinding?

  fun bindView(activity: ComponentActivity) {
    binding = ActivityOrcaBinding.inflate(activity.layoutInflater)
    unbindOnDestroy(activity)
  }

  private fun unbindOnDestroy(activity: ComponentActivity) {
    activity.lifecycle.addObserver(
      object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
          binding = null
        }
      }
    )
  }
}

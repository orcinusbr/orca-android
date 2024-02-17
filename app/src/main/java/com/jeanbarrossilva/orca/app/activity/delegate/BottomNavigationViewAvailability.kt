/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.app.activity.delegate

import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.jeanbarrossilva.orca.app.R
import com.jeanbarrossilva.orca.platform.autos.reactivity.OnBottomAreaAvailabilityChangeListener
import kotlinx.coroutines.flow.MutableStateFlow

internal interface BottomNavigationViewAvailability :
  OnBottomAreaAvailabilityChangeListener, Binding {
  override val yOffsetFlow: MutableStateFlow<Float>

  var constraintSet: ConstraintSet?

  override val height
    get() = binding?.bottomNavigationView?.height ?: 0

  override fun onBottomAreaAvailabilityChange(yOffset: Float) {
    constraintSet?.apply {
      getConstraint(R.id.container).layout.bottomMargin = -yOffset.toInt()
      getConstraint(R.id.bottom_navigation_view).transform.translationY = yOffset
      applyTo(binding?.root)
      yOffsetFlow.value = yOffset
    }
  }

  fun offsetOnChange(activity: ComponentActivity) {
    constraintSet = ConstraintSet().apply { clone(binding?.root) }
    dereferenceConstraintSetOnDestroy(activity)
  }

  private fun dereferenceConstraintSetOnDestroy(activity: ComponentActivity) {
    activity.lifecycle.addObserver(
      object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
          constraintSet = null
        }
      }
    )
  }
}

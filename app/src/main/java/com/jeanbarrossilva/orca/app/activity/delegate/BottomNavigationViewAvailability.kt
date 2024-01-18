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

internal interface BottomNavigationViewAvailability :
  OnBottomAreaAvailabilityChangeListener, Binding {
  var constraintSet: ConstraintSet?

  override val height
    get() = binding?.bottomNavigationView?.height ?: 0

  override fun getCurrentOffsetY(): Float {
    return constraintSet?.getConstraint(R.id.bottom_navigation_view)?.transform?.translationY ?: 0f
  }

  override fun onBottomAreaAvailabilityChange(offsetY: Float) {
    constraintSet?.apply {
      getConstraint(R.id.container).layout.bottomMargin = -offsetY.toInt()
      getConstraint(R.id.bottom_navigation_view).transform.translationY = offsetY
      applyTo(binding?.mainView)
    }
  }

  fun offsetOnChange(activity: ComponentActivity) {
    constraintSet = ConstraintSet().apply { clone(binding?.mainView) }
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

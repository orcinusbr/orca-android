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

package com.jeanbarrossilva.orca.feature.onboarding

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.composite.composable.ComposableActivity
import com.jeanbarrossilva.orca.ext.intents.intentOf
import com.jeanbarrossilva.orca.feature.onboarding.ui.Movement
import com.jeanbarrossilva.orca.feature.onboarding.ui.Onboarding
import com.jeanbarrossilva.orca.platform.navigation.extra

internal class OnboardingActivity : ComposableActivity() {
  private val movement by extra<Movement>(MOVEMENT_KEY)

  @Composable
  override fun Content() {
    Onboarding(movement, onNext = ::finish, onSkip = ::finish)
  }

  companion object {
    private const val MOVEMENT_KEY = "movement"

    fun getIntent(context: Context, movement: Movement): Intent {
      return intentOf<OnboardingActivity>(context, MOVEMENT_KEY to movement)
    }
  }
}

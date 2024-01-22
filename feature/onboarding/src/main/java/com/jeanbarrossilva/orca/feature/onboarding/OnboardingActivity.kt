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
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.composite.composable.ComposableActivity
import com.jeanbarrossilva.orca.ext.intents.intentOf
import com.jeanbarrossilva.orca.platform.animator.animation.Motion
import com.jeanbarrossilva.orca.platform.navigation.extra

internal class OnboardingActivity : ComposableActivity() {
  private val motion by extra<Motion>(MOTION_KEY)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
  }

  @Composable
  override fun Content() {
    Onboarding(motion, onNext = ::finish, onSkip = ::finish)
  }

  companion object {
    private const val MOTION_KEY = "motion"

    fun getIntent(context: Context, motion: Motion): Intent {
      return intentOf<OnboardingActivity>(context, MOTION_KEY to motion)
    }
  }
}

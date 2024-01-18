/*
 * Copyright © 2023 Orca
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

package com.jeanbarrossilva.orca.feature.composer

import android.content.Context
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.composite.composable.ComposableActivity
import com.jeanbarrossilva.orca.platform.starter.on

class ComposerActivity internal constructor() : ComposableActivity() {
  private val viewModel by viewModels<ComposerViewModel>()

  @Composable
  override fun Content() {
    Composer(viewModel, onBackwardsNavigation = ::finish)
  }

  companion object {
    fun start(context: Context) {
      context.on<ComposerActivity>().asNewTask().start()
    }
  }
}

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

package com.jeanbarrossilva.orca.feature.onboarding.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.jeanbarrossilva.orca.feature.onboarding.R

@Immutable
internal enum class Movement {
  Still {
    override val arePostPreviewsInitiallyVisible = true

    @Composable
    override fun rememberSloganAsState(): State<String> {
      return stringResource(R.string.feature_onboarding_slogan).let(::mutableStateOf)
    }

    @Composable
    override fun rememberIsButtonBarInitiallyVisibleAsState(): MutableState<Boolean> {
      return remember { mutableStateOf(true) }
    }
  },
  Animated {
    override val arePostPreviewsInitiallyVisible = false

    @Composable
    override fun rememberSloganAsState(): State<String> {
      return animateStringAsState(stringResource(R.string.feature_onboarding_slogan))
    }

    @Composable
    override fun rememberIsButtonBarInitiallyVisibleAsState(): MutableState<Boolean> {
      return remember { mutableStateOf(false) }
    }
  };

  abstract val arePostPreviewsInitiallyVisible: Boolean

  @Composable abstract fun rememberSloganAsState(): State<String>

  @Composable abstract fun rememberIsButtonBarInitiallyVisibleAsState(): MutableState<Boolean>
}

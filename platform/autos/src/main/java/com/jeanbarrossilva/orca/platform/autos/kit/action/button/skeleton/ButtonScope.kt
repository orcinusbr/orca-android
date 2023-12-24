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

package com.jeanbarrossilva.orca.platform.autos.kit.action.button.skeleton

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

/**
 * Scope through which the content of a [Button] can be loaded.
 *
 * @see load
 * @see Loadable
 */
internal class ButtonScope {
  /** Whether the content is currently loading. */
  private var isLoading by mutableStateOf(false)

  /**
   * Loads until the [action] finishes running.
   *
   * @param action Callback that executes the operation that is indicated by the loading.
   */
  inline fun load(action: () -> Unit) {
    load()
    action()
    isLoading = false
  }

  /** Loads indefinitely. */
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun load() {
    isLoading = true
  }

  /**
   * [Composable] that shows either the [content] or a [CircularProgressIndicator] if this
   * [ButtonScope] is currently loading.
   *
   * @param content [Composable] to be shown when the loading has finished.
   */
  @Composable
  fun Loadable(content: @Composable () -> Unit) {
    if (isLoading) {
      CircularProgressIndicator(
        Modifier.size(17.4.dp),
        LocalContentColor.current,
        strokeCap = StrokeCap.Round
      )
    } else {
      ProvideTextStyle(LocalTextStyle.current.copy(color = LocalContentColor.current)) { content() }
    }
  }
}

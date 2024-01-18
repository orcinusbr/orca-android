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

package com.jeanbarrossilva.orca.platform.autos.kit.sheet.controller

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.jeanbarrossilva.orca.platform.autos.kit.sheet.Sheet
import com.jeanbarrossilva.orca.platform.autos.kit.sheet.SheetScope

/**
 * Shows and hides a [Sheet].
 *
 * @param state [SheetState] to which a [Sheet]s' visibility changes will be requested.
 */
@OptIn(ExperimentalMaterial3Api::class)
class SheetController private constructor(internal val state: SheetState) {
  /** Content of the [Sheet] that is currently being shown. */
  internal var content by mutableStateOf<(SheetScope.() -> Unit)?>(null)
    private set

  /**
   * Shows a [Sheet].
   *
   * @param content Content to be displayed in the [Sheet].
   */
  suspend fun show(content: SheetScope.() -> Unit) {
    this.content = content
    state.expand()
  }

  /**
   * Hides a [Sheet] that has been shown.
   *
   * @see show
   */
  suspend fun hide() {
    state.hide()
    content = null
  }

  companion object {
    /**
     * [SheetController] to be used for displaying [Sheet]s.
     *
     * @see setCurrent
     */
    var current by mutableStateOf<SheetController?>(null)
      private set

    /**
     * Sets the current [SheetController] to one created with a remembered [SheetState] that holds
     * an initially-hidden [SheetState].
     *
     * @throws IllegalStateException If the current [SheetController] has already been set.
     * @see current
     * @see LocalContext
     * @see rememberBottomSheetScaffoldState
     * @see rememberModalBottomSheetState
     */
    @Composable
    @Suppress("ComposableNaming")
    @Throws(IllegalStateException::class)
    fun setCurrent() {
      rememberModalBottomSheetState().let {
        DisposableEffect(it) {
          setCurrent(it)
          onDispose { unsetCurrent() }
        }
      }
    }

    /**
     * Sets the current [SheetController] to one created with the given [state].
     *
     * @param state [SheetState] to which a [Sheet]'s visibility changes will be requested.
     * @throws IllegalStateException If the current [SheetController] has already been set.
     * @see current
     */
    @Throws(IllegalStateException::class)
    fun setCurrent(state: SheetState) {
      if (current == null) {
        current = SheetController(state)
      } else {
        throw IllegalStateException("Current sheet controller has already been set.")
      }
    }

    /**
     * Remembers either the [SheetState] of the current [SheetController] or one that is always
     * hidden if the current [SheetController] hasn't been set.
     *
     * @see SheetController.state
     * @see current
     * @see SheetState.Companion.AlwaysHidden
     * @see setCurrent
     */
    @Composable
    internal fun rememberCurrentOrHiddenState(): SheetState {
      return current?.state.let { remember(it) { it ?: SheetState.AlwaysHidden } }
    }

    /**
     * Dereferences the current [SheetController].
     *
     * @see current
     */
    internal fun unsetCurrent() {
      current = null
    }
  }
}

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

package br.com.orcinus.orca.platform.ime.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import br.com.orcinus.orca.platform.ime.Ime
import br.com.orcinus.orca.platform.ime.windowInsetsControllerCompat

/**
 * Remembers a [State] whose value is updated according to changes in the visibility of the IME.
 *
 * @see Ime
 */
@Composable
fun rememberImeAsState(): State<Ime> {
  val view = LocalView.current
  val state = remember { mutableStateOf(Ime.Unknown) }
  val listener = remember {
    object : OnImeVisibilityChangeListener(view) {
      override fun onImeVisibilityChange(ime: Ime) {
        state.value = ime
      }
    }
  }
  val windowInsetsController = remember(view, view::windowInsetsControllerCompat)

  DisposableEffect(view, listener) {
    windowInsetsController?.addOnControllableInsetsChangedListener(listener)
    onDispose {
      state.value = Ime.Unknown
      windowInsetsController?.removeOnControllableInsetsChangedListener(listener)
    }
  }

  return state
}

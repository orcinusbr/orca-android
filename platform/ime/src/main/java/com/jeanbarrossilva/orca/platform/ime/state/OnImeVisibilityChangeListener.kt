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

package com.jeanbarrossilva.orca.platform.ime.state

import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.jeanbarrossilva.orca.platform.ime.Ime
import com.jeanbarrossilva.orca.platform.ime.rootWindowInsetsCompat

/**
 * [WindowInsetsControllerCompat.OnControllableInsetsChangedListener] that listens to changes in the
 * visibility of the IME.
 *
 * @param view [View] whose [WindowInsetsCompat] will provide the visibility.
 * @see Ime
 */
abstract class OnImeVisibilityChangeListener(protected val view: View) :
  WindowInsetsControllerCompat.OnControllableInsetsChangedListener {
  final override fun onControllableInsetsChanged(
    controller: WindowInsetsControllerCompat,
    typeMask: Int
  ) {
    val ime = Ime.from(view.rootWindowInsetsCompat)
    onImeVisibilityChange(ime)
  }

  /**
   * Callback called when the [Insets] of the IME have changed because of a toggle in its
   * visibility.
   *
   * @param ime [Ime] representing the visibility to which it changed.
   */
  @JvmName("onImeVisibilityChange")
  @Suppress("INAPPLICABLE_JVM_NAME")
  protected abstract fun onImeVisibilityChange(ime: Ime)
}

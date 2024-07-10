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

package br.com.orcinus.orca.platform.autos.kit.input.text.composition

import br.com.orcinus.orca.platform.autos.InternalPlatformAutosApi
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.CompositionTextFieldValue

/**
 * Listener to be notified of changes in the value of a [CompositionTextField].
 *
 * @see onValueChange
 */
fun interface OnValueChangeListener {
  /**
   * Callback called whenever the value changes.
   *
   * @param value [CompositionTextFieldValue] representing the current textual state.
   */
  @InternalPlatformAutosApi fun onValueChange(value: CompositionTextFieldValue)
}

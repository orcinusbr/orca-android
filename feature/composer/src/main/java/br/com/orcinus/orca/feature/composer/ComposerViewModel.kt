/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.feature.composer

import androidx.lifecycle.ViewModel
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.CompositionTextFieldValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class ComposerViewModel : ViewModel() {
  private val textFieldValueMutableFlow = MutableStateFlow(CompositionTextFieldValue.Empty)

  val textFieldValueFlow = textFieldValueMutableFlow.asStateFlow()

  fun setTextFieldValue(textFieldValue: CompositionTextFieldValue) {
    textFieldValueMutableFlow.value = textFieldValue
  }

  fun compose() {}
}

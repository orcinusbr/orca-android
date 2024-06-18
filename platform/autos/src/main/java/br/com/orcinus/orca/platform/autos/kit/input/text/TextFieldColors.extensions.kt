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

package br.com.orcinus.orca.platform.autos.kit.input.text

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.ui.graphics.Color
import br.com.orcinus.orca.ext.reflection.access
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions

/**
 * Color by which the container of a text field should be colored.
 *
 * @param isEnabled Whether the text field is enabled.
 * @param isInvalid Whether the text field is in an invalid state.
 * @param interactionSource [InteractionSource] to which [Interaction]s with the text field are
 *   sent.
 * @throws NoSuchMethodException If, because this uses reflection under the hood, the method that
 *   returns the [State] by which the container [Color] is held is not found.
 */
@Composable
@Throws(NoSuchMethodException::class)
internal fun TextFieldColors.containerColor(
  isEnabled: Boolean,
  isInvalid: Boolean,
  interactionSource: InteractionSource
): State<Color> {
  val composer = currentComposer
  val key = currentCompositeKeyHash
  return try {
    TextFieldColors::class
      .declaredMemberFunctions
      .filterIsInstance<KFunction<State<Color>>>()
      .single { it.name.startsWith("containerColor") }
      .access { call(this@containerColor, isEnabled, isInvalid, interactionSource, composer, key) }
  } catch (_: NoSuchElementException) {
    throw NoSuchMethodException(
      "TextFieldColors.containerColor(Boolean, Boolean, InteractionSource, Composer, Int): " +
        "State<Color>"
    )
  }
}

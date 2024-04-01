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

package com.jeanbarrossilva.orca.platform.autos.kit.action.button.icon

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * [MutableInteractionSource] that ignores all specified [Interaction]s.
 *
 * @param ignored [KClass]es of the [Interaction]s to be ignored.
 */
@Suppress("FunctionName")
fun IgnoringMutableInteractionSource(
  vararg ignored: KClass<out Interaction>
): MutableInteractionSource {
  return object : MutableInteractionSource {
    /** Whether this [Interaction] is ignored. */
    private val Interaction.isIgnored
      get() = ignored.any(this::class::isSubclassOf)

    override val interactions = MutableSharedFlow<Interaction>()

    override suspend fun emit(interaction: Interaction) {
      if (!interaction.isIgnored) {
        this.interactions.emit(interaction)
      }
    }

    override fun tryEmit(interaction: Interaction): Boolean {
      return !interaction.isIgnored && interactions.tryEmit(interaction)
    }
  }
}

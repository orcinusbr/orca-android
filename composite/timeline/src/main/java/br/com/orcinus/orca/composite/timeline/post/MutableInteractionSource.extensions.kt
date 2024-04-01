/*
 * Copyright © 2023-2024 Orcinus
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

package br.com.orcinus.orca.composite.timeline.post

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import kotlinx.coroutines.flow.emptyFlow

/** [MutableInteractionSource] that doesn't emit the [Interaction]s that are sent to it. */
@Suppress("FunctionName")
internal fun EmptyMutableInteractionSource(): MutableInteractionSource {
  return object : MutableInteractionSource {
    override val interactions = emptyFlow<Interaction>()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction): Boolean {
      return false
    }
  }
}

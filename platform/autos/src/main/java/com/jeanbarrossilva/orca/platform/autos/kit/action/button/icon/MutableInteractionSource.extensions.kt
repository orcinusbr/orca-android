/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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

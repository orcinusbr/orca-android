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

package com.jeanbarrossilva.orca.platform.autos.kit.input.option.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.jeanbarrossilva.orca.platform.autos.kit.input.option.CoreOption
import com.jeanbarrossilva.orca.platform.autos.kit.input.option.Option

/**
 * Scope through which [Option]s can be added.
 *
 * @param onSelectionToggle Callback run whenever any of the [Option]s is selected.
 */
class OptionsScope internal constructor(private val onSelectionToggle: (index: Int) -> Unit) {
  /** [Option]s that have been added. */
  private val mutableOptions =
    mutableStateListOf<@Composable (selectedOptionIndex: Int, Shape) -> Unit>()

  /** Immutable [List] with the [Option]s that have been added. */
  internal val options
    get() = mutableOptions.toList()

  /**
   * Adds an [Option].
   *
   * @param modifier [Modifier] to be applied to the [Option].
   * @param content [Text] that's the label.
   */
  fun option(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    with(mutableOptions.size) index@{
      mutableOptions.add { selectedOptionIndex, shape ->
        CoreOption(
          isSelected = this@index == selectedOptionIndex,
          onSelectionToggle = { isSelected ->
            if (isSelected) {
              onSelectionToggle(this@index)
            }
          },
          modifier,
          shape,
          content
        )
      }
    }
  }

  /**
   * Adds a loading [Option].
   *
   * @param modifier [Modifier] to be applied to the [Option].
   */
  internal fun option(modifier: Modifier = Modifier) {
    mutableOptions.add { _, shape -> CoreOption(modifier, shape) }
  }
}

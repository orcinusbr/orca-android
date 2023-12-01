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

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import com.jeanbarrossilva.orca.platform.autos.border
import com.jeanbarrossilva.orca.platform.autos.kit.bottom
import com.jeanbarrossilva.orca.platform.autos.kit.input.option.Option
import com.jeanbarrossilva.orca.platform.autos.kit.input.option.OptionDefaults
import com.jeanbarrossilva.orca.platform.autos.kit.top
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/** Tag that identifies [Options]' [Divider]s for testing purposes. */
internal const val OPTIONS_DIVIDER_TAG = "options-divider"

/**
 * [Column] of loading [Option]s that are shaped according to their position.
 *
 * @param modifier [Modifier] to be applied to the underlying [Column].
 */
@Composable
fun Options(modifier: Modifier = Modifier) {
  Options(onSelection = {}, modifier) { repeat(128) { option() } }
}

/**
 * [Column] of [Option]s that are shaped according to their position and can be singly selected.
 *
 * @param onSelection Callback run whenever any of the [Option]s is selected.
 * @param modifier [Modifier] to be applied to the underlying [Column].
 * @param content Actions to be run on the given [OptionsScope].
 */
@Composable
fun Options(
  onSelection: (index: Int) -> Unit,
  modifier: Modifier = Modifier,
  content: OptionsScope.() -> Unit
) {
  val defaultOptionShape = OptionDefaults.shape
  var selectedOptionIndex by remember { mutableIntStateOf(0) }
  val scope =
    remember(onSelection, content) { OptionsScope { selectedOptionIndex = it }.apply(content) }

  DisposableEffect(selectedOptionIndex) {
    onSelection(selectedOptionIndex)
    onDispose {}
  }

  Column(modifier.border(defaultOptionShape)) {
    scope.options.forEachIndexed { index, option ->
      when {
        scope.options.size == 1 -> option(selectedOptionIndex, defaultOptionShape)
        index == 0 -> option(selectedOptionIndex, defaultOptionShape.top)
        index == scope.options.lastIndex -> {
          option(selectedOptionIndex, defaultOptionShape.bottom)
          return@forEachIndexed
        }
        else -> option(selectedOptionIndex, RectangleShape)
      }

      Divider(Modifier.testTag(OPTIONS_DIVIDER_TAG))
    }
  }
}

/**
 * [Column] of [Option]s that are shaped according to their position.
 *
 * @param count Amount of sample [Option]s to be added.
 * @param onSelection Callback run whenever any of the [Option]s is selected.
 * @param modifier [Modifier] to be applied to the underlying [Column].
 */
@Composable
internal fun SampleOptions(
  modifier: Modifier = Modifier,
  count: Int = 3,
  onSelection: (index: Int) -> Unit = { _ -> }
) {
  Options(onSelection, modifier) { repeat(count) { option { Text("Label #$it") } } }
}

@Composable
@MultiThemePreview
private fun LoadingOptionsPreview() {
  AutosTheme { Options() }
}

@Composable
@MultiThemePreview
private fun LoadedOptionsPreview() {
  AutosTheme { SampleOptions() }
}

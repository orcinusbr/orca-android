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

package com.jeanbarrossilva.orca.platform.autos.kit.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Popup
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/**
 * [Popup] menu that displays a variety of options through [DropdownMenuItem]s in its [content].
 *
 * An Orca-specific version of [androidx.compose.material3.DropdownMenu].
 *
 * @param isExpanded Whether it's being shown.
 * @param onDismissal Callback run when it is requested to be dismissed.
 * @param modifier [Modifier] to be applied to the underlying [DropdownMenu].
 * @param content [DropdownMenuItem]s contained by this
 *   [DropdownMenu][com.jeanbarrossilva.orca.platform.ui.component.menu.DropdownMenu].
 */
@Composable
fun DropdownMenu(
  isExpanded: Boolean,
  onDismissal: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable ColumnScope.() -> Unit
) {
  DropdownMenu(
    isExpanded,
    onDismissal,
    modifier.background(AutosTheme.colors.surface.container.asColor),
    content = content
  )
}

/**
 * Preview of a [DropdownMenu][com.jeanbarrossilva.orca.platform.ui.component.menu.DropdownMenu].
 */
@Composable
@MultiThemePreview
private fun DropdownMenuPreview() {
  AutosTheme {
    DropdownMenu(isExpanded = true, onDismissal = {}) {
      repeat(8) { DropdownMenuItem(text = { Text("Item $it") }, onClick = {}) }
    }
  }
}

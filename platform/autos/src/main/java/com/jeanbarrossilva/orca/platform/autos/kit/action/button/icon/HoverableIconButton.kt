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

import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.action.Hoverable
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/**
 * [IconButton] that gets visually highlighted when it's hovered.
 *
 * @param onClick Callback run whenever this [HoverableIconButton] is clicked.
 * @param modifier [Modifier] to be applied to the underlying [Hoverable].
 * @param content [Icon] to be shown.
 */
@Composable
fun HoverableIconButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  val interactionSource = remember {
    IgnoringMutableInteractionSource(PressInteraction.Press::class, HoverInteraction::class)
  }

  Hoverable(modifier) {
    IconButton(onClick, interactionSource = interactionSource, content = content)
  }
}

@Composable
@MultiThemePreview
private fun HoverableIconButtonPreview() {
  AutosTheme {
    HoverableIconButton(onClick = {}) {
      Icon(AutosTheme.iconography.home.outlined.asImageVector, contentDescription = "Home")
    }
  }
}

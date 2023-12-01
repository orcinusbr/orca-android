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

package com.jeanbarrossilva.orca.platform.autos.kit.action.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/**
 * [ElevatedButton] that represents a primary action, performed or requested to be performed through
 * [onClick]; usually is placed on the bottom of the screen, filling its width.
 *
 * @param onClick Callback called whenever it's clicked.
 * @param modifier [Modifier] to be applied to the underlying [ElevatedButton].
 * @param isEnabled Whether it can be interacted with.
 * @param content Content to be placed inside of it; generally a [Text] that shortly explains the
 *   action performed by [onClick].
 */
@Composable
fun PrimaryButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  isEnabled: Boolean = true,
  content: @Composable () -> Unit
) {
  val enabledContentColor = AutosTheme.colors.primary.content.asColor
  val disabledContentColor = AutosTheme.colors.disabled.content.asColor
  val contentColor =
    remember(isEnabled, enabledContentColor, disabledContentColor) {
      if (isEnabled) enabledContentColor else disabledContentColor
    }
  var isLoading by remember { mutableStateOf(false) }

  ElevatedButton(
    onClick = {
      isLoading = true
      onClick()
      isLoading = false
    },
    modifier,
    isEnabled,
    shape = AutosTheme.forms.medium.asShape,
    colors =
      ButtonDefaults.elevatedButtonColors(
        AutosTheme.colors.primary.container.asColor,
        enabledContentColor,
        AutosTheme.colors.disabled.container.asColor,
        disabledContentColor
      ),
    contentPadding = PaddingValues(AutosTheme.spacings.large.dp)
  ) {
    if (isLoading) {
      CircularProgressIndicator(Modifier.size(17.4.dp), contentColor, strokeCap = StrokeCap.Round)
    } else {
      ProvideTextStyle(LocalTextStyle.current.copy(color = contentColor), content)
    }
  }
}

@Composable
@MultiThemePreview
private fun DisabledPrimaryButtonPreview() {
  AutosTheme { PrimaryButton(isEnabled = false) }
}

/** Preview of an enabled [PrimaryButton]. */
@Composable
@MultiThemePreview
private fun EnabledPrimaryButtonPreview() {
  AutosTheme { PrimaryButton(isEnabled = true) }
}

/**
 * [ElevatedButton] that represents a primary action; usually is placed on the bottom of the screen,
 * filling its width.
 *
 * @param isEnabled Whether it can be interacted with.
 * @param modifier [Modifier] to be applied to the underlying [ElevatedButton].
 */
@Composable
private fun PrimaryButton(isEnabled: Boolean, modifier: Modifier = Modifier) {
  PrimaryButton(onClick = {}, modifier, isEnabled) { Text("Label") }
}

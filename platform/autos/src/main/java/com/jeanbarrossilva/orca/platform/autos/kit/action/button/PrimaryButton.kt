/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.autos.kit.action.button

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.skeleton.Button
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.skeleton.ButtonDefaults as _ButtonDefaults
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/** Tag that identifies a [PrimaryButton] for testing purposes. */
const val PRIMARY_BUTTON_TAG = "primary-button"

/**
 * [Button] that represents a primary action, performed or requested to be performed through
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
  Button {
    ElevatedButton(
      onClick = { load(onClick) },
      modifier.testTag(PRIMARY_BUTTON_TAG),
      isEnabled,
      _ButtonDefaults.shape,
      colors =
        ButtonDefaults.elevatedButtonColors(
          AutosTheme.colors.primary.container.asColor,
          AutosTheme.colors.primary.content.asColor,
          AutosTheme.colors.disabled.container.asColor,
          AutosTheme.colors.disabled.content.asColor
        ),
      contentPadding = _ButtonDefaults.padding
    ) {
      Loadable(content)
    }
  }
}

/** Preview of a disabled [PrimaryButton]. */
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
 * [Button] that represents a primary action; usually is placed on the bottom of the screen, filling
 * its width.
 *
 * @param isEnabled Whether it can be interacted with.
 * @param modifier [Modifier] to be applied to the underlying [ElevatedButton].
 */
@Composable
private fun PrimaryButton(isEnabled: Boolean, modifier: Modifier = Modifier) {
  PrimaryButton(onClick = {}, modifier, isEnabled) { Text("Label") }
}

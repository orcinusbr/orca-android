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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.skeleton.Button as _Button
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.skeleton.ButtonDefaults as _ButtonDefaults
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.ButtonBar
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/** Tag that identifies the [SecondaryButton] for testing purposes. */
const val SECONDARY_BUTTON_TAG = "secondary-button"

/**
 * [Button][_Button] that executes a secondary action when clicked, and is usually preceded by a
 * [PrimaryButton] within a [ButtonBar].
 *
 * @param onClick Callback called whenever it's clicked.
 * @param modifier [Modifier] to be applied to the underlying [_Button].
 * @param content Content to be placed inside of it; generally a [Text] that shortly explains the
 *   action performed by [onClick].
 */
@Composable
fun SecondaryButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  _Button {
    Button(
      onClick = { load(onClick) },
      modifier.testTag(SECONDARY_BUTTON_TAG),
      shape = _ButtonDefaults.shape,
      colors =
        ButtonDefaults.buttonColors(
          containerColor = AutosTheme.colors.placeholder.asColor,
          contentColor = AutosTheme.colors.background.content.asColor
        ),
      contentPadding = _ButtonDefaults.padding
    ) {
      Loadable(content)
    }
  }
}

@Composable
@MultiThemePreview
private fun SecondaryButtonPreview() {
  AutosTheme {
    SecondaryButton(onClick = { runBlocking { delay(5.seconds) } }, Modifier.fillMaxWidth()) {
      Text("Label")
    }
  }
}

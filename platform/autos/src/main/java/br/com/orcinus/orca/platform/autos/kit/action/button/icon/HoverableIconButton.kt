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

package br.com.orcinus.orca.platform.autos.kit.action.button.icon

import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.action.Hoverable
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/** Default values used by a [HoverableIconButton]. */
object HoverableIconButtonDefaults {
  /** Default size of a [HoverableIconButton]. */
  val Size = DpSize(width = 48.dp, height = 48.dp)
}

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
  content: @Composable BoxScope.() -> Unit
) {
  val interactionSource = remember {
    IgnoringMutableInteractionSource(PressInteraction.Press::class, HoverInteraction::class)
  }

  Hoverable(modifier.size(HoverableIconButtonDefaults.Size)) {
    IconButton(onClick, Modifier.matchParentSize(), interactionSource = interactionSource) {
      content()
    }
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

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

package com.jeanbarrossilva.orca.feature.composer.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

@Composable
internal fun FormatIconButton(
  isEnabled: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  val role = Role.Switch
  val contentColor by
    animateColorAsState(
      if (isEnabled) AutosTheme.colors.primary.container.asColor else LocalContentColor.current,
      label = "ContentColor"
    )

  Box(
    modifier
      .clip(AutosTheme.forms.small.asShape)
      .clickable(role = role, onClick = onClick)
      .padding(4.dp)
      .size(24.dp)
      .semantics {
        this.role = role
        toggleableState = if (isEnabled) ToggleableState.On else ToggleableState.Off
      },
    Alignment.Center
  ) {
    CompositionLocalProvider(LocalContentColor provides contentColor, content = content)
  }
}

@Composable
@MultiThemePreview
private fun DisabledFormatIconButtonPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      FormatIconButton(isEnabled = false)
    }
  }
}

@Composable
@MultiThemePreview
private fun EnabledFormatIconButtonPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      FormatIconButton(isEnabled = true)
    }
  }
}

@Composable
private fun FormatIconButton(isEnabled: Boolean, modifier: Modifier = Modifier) {
  FormatIconButton(isEnabled, onClick = {}, modifier) {
    Icon(
      if (isEnabled) {
        AutosTheme.iconography.compose.filled.asImageVector
      } else {
        AutosTheme.iconography.compose.outlined.asImageVector
      },
      contentDescription = "Compose"
    )
  }
}

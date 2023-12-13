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

package com.jeanbarrossilva.orca.platform.ui.component.stat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.action.Hoverable
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

internal object StatDefaults {
  val IconSize = ActivateableStatIconDefaults.Size

  val contentColor
    @Composable get() = AutosTheme.colors.secondary.asColor
}

internal enum class StatPosition {
  LEADING {
    override val padding
      @Composable get() = PaddingValues(end = AutosTheme.spacings.small.dp)
  },
  SUBSEQUENT {
    override val padding
      @Composable get() = PaddingValues(horizontal = AutosTheme.spacings.small.dp)
  },
  TRAILING {
    override val padding
      @Composable get() = PaddingValues(start = AutosTheme.spacings.small.dp)
  };

  @get:Composable abstract val padding: PaddingValues
}

@Composable
internal fun Stat(
  position: StatPosition,
  vector: ImageVector,
  contentDescription: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  contentColor: Color = StatDefaults.contentColor,
  label: @Composable () -> Unit = {}
) {
  Stat(position, onClick, modifier, contentColor) {
    Icon(vector, contentDescription, Modifier.size(StatDefaults.IconSize))
    label()
  }
}

@Composable
internal fun Stat(
  position: StatPosition,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  contentColor: Color = StatDefaults.contentColor,
  content: @Composable RowScope.() -> Unit
) {
  val spacing = AutosTheme.spacings.small.dp

  Hoverable(modifier.padding(position.padding).clickable(role = Role.Button, onClick = onClick)) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(spacing),
      verticalAlignment = Alignment.CenterVertically
    ) {
      CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides AutosTheme.typography.bodySmall.copy(color = contentColor)
      ) {
        content()
      }
    }
  }
}

@Composable
@MultiThemePreview
internal fun LeadingStatPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) { Stat(StatPosition.LEADING) }
  }
}

@Composable
@MultiThemePreview
internal fun SubsequentStatPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      Stat(StatPosition.SUBSEQUENT)
    }
  }
}

@Composable
@MultiThemePreview
internal fun TrailingStatPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) { Stat(StatPosition.TRAILING) }
  }
}

@Composable
private fun Stat(position: StatPosition, modifier: Modifier = Modifier) {
  Stat(
    position,
    AutosTheme.iconography.comment.outlined.asImageVector,
    contentDescription = "Comment",
    onClick = {},
    modifier
  ) {
    Text("8")
  }
}

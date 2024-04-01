/*
 * Copyright © 2023-2024 Orca
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

package br.com.orcinus.orca.composite.timeline.stat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.action.Hoverable
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/** Default values used by a [Stat]. */
internal object StatDefaults {
  /** Amount of [Dp]s by which the [Icon] of a [Stat] is sized by default. */
  val IconSize = 18.dp

  /** [Color] by which the content of a [Stat] is colored by default. */
  val contentColor
    @Composable get() = AutosTheme.colors.secondary.asColor
}

/**
 * [Composable] that represents a [Post]'s
 * [Stat][br.com.orcinus.orca.core.feed.profile.post.stat.Stat].
 *
 * @param position [StatPosition] that indicates how it is positioned.
 * @param vector [ImageVector] to be displayed by the [Icon].
 * @param contentDescription Describes the content of the [Icon].
 * @param onClick Callback run whenever it is clicked.
 * @param modifier [Modifier] to be applied to the underlying [Hoverable].
 * @param contentColor Color by which both the [Icon] and the [label] will be colored.
 * @param label [Text] that either describes the [Stat] or displays its count.
 */
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

/**
 * [Composable] that represents a [Post]'s
 * [Stat][br.com.orcinus.orca.core.feed.profile.post.stat.Stat].
 *
 * @param position [StatPosition] that indicates how it is positioned.
 * @param onClick Callback run whenever it is clicked.
 * @param modifier [Modifier] to be applied to the underlying [Hoverable].
 * @param contentColor Color by which the [content] will be colored.
 */
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

/**
 * Preview of a leading [Stat].
 *
 * @see StatPosition.Leading
 */
@Composable
@MultiThemePreview
internal fun LeadingStatPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) { Stat(StatPosition.Leading) }
  }
}

/**
 * Preview of a subsequent [Stat].
 *
 * @see StatPosition.Subsequent
 */
@Composable
@MultiThemePreview
internal fun SubsequentStatPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      Stat(StatPosition.Subsequent)
    }
  }
}

/**
 * Preview of a trailing [Stat].
 *
 * @see StatPosition.Trailing
 */
@Composable
@MultiThemePreview
internal fun TrailingStatPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) { Stat(StatPosition.Trailing) }
  }
}

/**
 * [Composable] that represents a [Post]'s
 * [Stat][br.com.orcinus.orca.core.feed.profile.post.stat.Stat].
 *
 * @param position [StatPosition] that indicates how it is positioned.
 * @param modifier [Modifier] to be applied to the underlying [Hoverable].
 */
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

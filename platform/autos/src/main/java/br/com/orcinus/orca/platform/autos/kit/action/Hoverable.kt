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

package br.com.orcinus.orca.platform.autos.kit.action

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.forms.asShape
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/**
 * Visually highlights the [content] when it gets hovered; meant for indicating the non-obvious
 * actionability of a [Composable].
 *
 * @param modifier [Modifier] to be applied to the underlying [Box].
 * @param content Content to be highlighted when hovered.
 */
@Composable
fun Hoverable(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
  val interactionSource = remember(::MutableInteractionSource)
  val isHovered by interactionSource.collectIsHoveredAsState()

  Hoverable(isHovered, modifier.hoverable(interactionSource), content)
}

/**
 * Visually highlights the [content] if [isHighlighted] is `true`; meant for indicating the
 * non-obvious actionability of a [Composable].
 *
 * @param isHighlighted Whether the [content] is highlighted.
 * @param modifier [Modifier] to be applied to the underlying [Box].
 * @param content Content to be highlighted.
 */
@Composable
private fun Hoverable(
  isHighlighted: Boolean,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  val density = LocalDensity.current
  val animationSpec = tween<Float>(durationMillis = 128)
  val alpha by animateFloatAsState(if (isHighlighted) 1f else 0f, animationSpec, label = "Alpha")
  val containerColor = AutosTheme.colors.placeholder.asColor
  val shape = AutosTheme.forms.large.asShape
  val spacing = AutosTheme.spacings.small.dp
  val spacingInPx = remember(density, spacing) { with(density) { spacing.toPx() } }
  val scale by animateFloatAsState(if (isHighlighted) .95f else 1f, animationSpec, label = "Scale")

  Box(
    Modifier.drawBehind {
        drawRoundRect(
          containerColor,
          topLeft = Offset(x = -spacingInPx, y = -spacingInPx),
          size =
            size.copy(width = size.width + spacingInPx * 2, height = size.height + spacingInPx * 2),
          cornerRadius = CornerRadius(shape.topStart.toPx(size, density)),
          alpha = alpha
        )
      }
      .scale(scale)
      .then(modifier)
  ) {
    content()
  }
}

/** Preview of an idle [Hoverable]. */
@Composable
@MultiThemePreview
private fun IdleHoverablePreview() {
  AutosTheme { Hoverable(isHighlighted = false) }
}

/** Preview of a highlighted [Hoverable]. */
@Composable
@MultiThemePreview
private fun HighlightedHoverablePreview() {
  AutosTheme { Column { Hoverable(isHighlighted = true) } }
}

/**
 * Visually highlights the content if [isHighlighted] is `true`; meant for indicating the
 * non-obvious actionability of a [Composable].
 *
 * @param isHighlighted Whether the content is highlighted.
 * @param modifier [Modifier] to be applied to the underlying [Box].
 */
@Composable
private fun Hoverable(isHighlighted: Boolean, modifier: Modifier = Modifier) {
  Hoverable(isHighlighted, modifier) {
    IconButton(onClick = {}) {
      Icon(AutosTheme.iconography.home.outlined.asImageVector, contentDescription = "Home")
    }
  }
}

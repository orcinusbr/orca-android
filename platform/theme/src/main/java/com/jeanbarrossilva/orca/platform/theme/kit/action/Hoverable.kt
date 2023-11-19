package com.jeanbarrossilva.orca.platform.theme.kit.action

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
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.theme.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.theme.autos.iconography.asImageVector

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
  val containerColor = OrcaTheme.colors.placeholder.asColor
  val shape = OrcaTheme.forms.large.asShape
  val spacing = OrcaTheme.spacings.small.dp
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
  OrcaTheme { Hoverable(isHighlighted = false) }
}

/** Preview of a highlighted [Hoverable]. */
@Composable
@MultiThemePreview
private fun HighlightedHoverablePreview() {
  OrcaTheme { Column { Hoverable(isHighlighted = true) } }
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
      Icon(OrcaTheme.iconography.home.outlined.asImageVector, contentDescription = "Home")
    }
  }
}

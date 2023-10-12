package com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.kit.action.button.PrimaryButton
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.button.placement.Orientation
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.button.placement.height
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.button.placement.mapToPlacement
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.button.placement.place

/** Tag that identifies a [ButtonBar] for testing purposes. */
internal const val BUTTON_BAR_TAG = "button-bar"

/** Tag that identifies a [ButtonBar]'s [Divider] for testing purposes. */
internal const val BUTTON_BAR_DIVIDER_TAG = "button-bar-divider"

/**
 * Bar for housing a [PrimaryButton].
 *
 * @param lazyListState [LazyListState] that will determine whether it gets highlighted.
 * @param modifier [Modifier] to be applied to the underlying [Layout].
 * @param content [PrimaryButton] to be shown.
 */
@Composable
fun ButtonBar(
  lazyListState: LazyListState,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  val isHighlighted by remember(lazyListState) { derivedStateOf(lazyListState::canScrollForward) }
  ButtonBar(isHighlighted, modifier, content)
}

/**
 * Bar for housing a [PrimaryButton] in an idle state.
 *
 * @param modifier [Modifier] to be applied to the underlying [Layout].
 * @param content [PrimaryButton] to be shown.
 */
@Composable
fun ButtonBar(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
  ButtonBar(isHighlighted = false, modifier, content)
}

/**
 * Bar for housing a [PrimaryButton].
 *
 * @param isHighlighted Whether it should visually stand out.
 * @param modifier [Modifier] to be applied to the underlying [Layout].
 * @param content [PrimaryButton] to be shown.
 */
@Composable
private fun ButtonBar(
  isHighlighted: Boolean,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  val density = LocalDensity.current
  val border = OrcaTheme.borders.default
  val borderStrokeWidth by
    animateDpAsState(if (isHighlighted) border.width else (-1).dp, label = "BorderStrokeWidth")
  val spacing = OrcaTheme.spacings.medium
  val spacingInPx = remember(density, spacing) { with(density) { spacing.roundToPx() } }
  val containerColor by
    animateColorAsState(
      if (isHighlighted) {
        OrcaTheme.colors.surface.container
      } else {
        OrcaTheme.colors.background.container
      },
      label = "ContainerColor"
    )

  Column(modifier) {
    Divider(Modifier.testTag(BUTTON_BAR_DIVIDER_TAG), thickness = borderStrokeWidth)

    Layout(
      content,
      Modifier.drawBehind { drawRect(containerColor) }
        .padding(spacing)
        .fillMaxWidth()
        .testTag(BUTTON_BAR_TAG)
    ) { measurables, constraints ->
      val orientation = Orientation.VERTICAL
      val placements = measurables.mapToPlacement(constraints, orientation, spacingInPx)

      layout(constraints.maxWidth, placements.height) { place(placements, orientation) }
    }
  }
}

/**
 * Bar for housing a [PrimaryButton].
 *
 * @param lazyListState [LazyListState] that will determine whether it gets highlighted.
 * @param modifier [Modifier] to be applied to the underlying [Layout].
 */
@Composable
internal fun ButtonBar(lazyListState: LazyListState, modifier: Modifier = Modifier) {
  ButtonBar(lazyListState, modifier) { PrimaryButton(onClick = {}) { Text("Label") } }
}

/** Preview of an idle [ButtonBar]. */
@Composable
@MultiThemePreview
private fun IdleButtonBarPreview() {
  OrcaTheme { ButtonBar(isHighlighted = false) }
}

/** Preview of a highlighted [ButtonBar]. */
@Composable
@MultiThemePreview
private fun HighlightedButtonBarPreview() {
  OrcaTheme { ButtonBar(isHighlighted = true) }
}

/**
 * Bar for housing a [PrimaryButton].
 *
 * @param isHighlighted Whether it should visually stand out.
 * @param modifier [Modifier] to be applied to the underlying [Layout].
 */
@Composable
private fun ButtonBar(isHighlighted: Boolean, modifier: Modifier = Modifier) {
  ButtonBar(isHighlighted, modifier) { PrimaryButton(onClick = {}) { Text("Label") } }
}

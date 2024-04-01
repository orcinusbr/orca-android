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

package br.com.orcinus.orca.platform.autos.kit.scaffold.bar.button

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.platform.autos.borders.asBorderStroke
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.kit.action.button.PrimaryButton
import br.com.orcinus.orca.platform.autos.kit.action.button.SecondaryButton
import br.com.orcinus.orca.platform.autos.kit.action.button.skeleton.Button
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.button.placement.Orientation
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.button.placement.height
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.button.placement.mapToPlacement
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.button.placement.place
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/** Tag that identifies a [ButtonBar] for testing purposes. */
internal const val BUTTON_BAR_TAG = "button-bar"

/** Tag that identifies a [ButtonBar]'s [Divider] for testing purposes. */
internal const val BUTTON_BAR_DIVIDER_TAG = "button-bar-divider"

/** Default values of a [ButtonBar]. */
object ButtonBarDefaults {
  /** Amount of [Dp]s by which a [ButtonBar]'s [Button]s are spaced. */
  val spacing
    @Composable get() = AutosTheme.spacings.small.dp
}

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
  val border = AutosTheme.borders.default.asBorderStroke
  val borderStrokeWidth by
    animateDpAsState(if (isHighlighted) border.width else (-1).dp, label = "BorderStrokeWidth")
  val spacing = ButtonBarDefaults.spacing
  val spacingInPx = remember(density, spacing) { with(density) { spacing.roundToPx() } }
  val containerColor by
    animateColorAsState(
      if (isHighlighted) {
        AutosTheme.colors.surface.container.asColor
      } else {
        AutosTheme.colors.background.container.asColor
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

      layout(constraints.maxWidth, placements.height + spacingInPx) {
        place(placements, orientation)
      }
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
  ButtonBar(lazyListState, modifier) { SampleContent() }
}

/** Preview of an idle [ButtonBar]. */
@Composable
@MultiThemePreview
private fun IdleButtonBarPreview() {
  AutosTheme { ButtonBar(isHighlighted = false) }
}

/** Preview of a highlighted [ButtonBar]. */
@Composable
@MultiThemePreview
private fun HighlightedButtonBarPreview() {
  AutosTheme { ButtonBar(isHighlighted = true) }
}

/**
 * Bar for housing a [PrimaryButton].
 *
 * @param isHighlighted Whether it should visually stand out.
 * @param modifier [Modifier] to be applied to the underlying [Layout].
 */
@Composable
private fun ButtonBar(isHighlighted: Boolean, modifier: Modifier = Modifier) {
  ButtonBar(isHighlighted, modifier) { SampleContent() }
}

/** Sample [Button]s for a [ButtonBar]. */
@Composable
private fun SampleContent() {
  PrimaryButton(onClick = {}) { Text("Primary") }
  SecondaryButton(onClick = {}) { Text("Secondary") }
}

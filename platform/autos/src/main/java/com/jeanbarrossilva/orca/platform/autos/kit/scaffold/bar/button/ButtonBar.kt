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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.autos.borders.asBorderStroke
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.PrimaryButton
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.SecondaryButton
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.skeleton.Button
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.placement.Orientation
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.placement.height
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.placement.mapToPlacement
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.placement.place
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/** Tag that identifies a [ButtonBar] for testing purposes. */
internal const val BUTTON_BAR_TAG = "button-bar"

/** Tag that identifies a [ButtonBar]'s [Divider] for testing purposes. */
internal const val BUTTON_BAR_DIVIDER_TAG = "button-bar-divider"

/** Default values of a [ButtonBar]. */
object ButtonBarDefaults {
  /** [ButtonBarMaterial] by which a [ButtonBar] is made out of by default. */
  internal val Material = ButtonBarMaterial.Opaque

  /** Amount of [Dp]s by which a [ButtonBar]'s [Button]s are spaced. */
  val spacing
    @Composable get() = AutosTheme.spacings.small.dp

  /**
   * Creates [ButtonBarColors] by which a [ButtonBar] is colored by default.
   *
   * @param idleContainerColor [Color] by which the container should be colored when idle.
   * @param scrolledContainerColor [Color] by which the container should be colored when scrolled.
   */
  @Composable
  fun colors(
    idleContainerColor: Color = ButtonBarColors.defaultIdleContainer,
    scrolledContainerColor: Color = AutosTheme.colors.surface.container.asColor
  ): ButtonBarColors {
    return ButtonBarColors(idleContainerColor, scrolledContainerColor)
  }
}

/**
 * Bar for housing a [PrimaryButton].
 *
 * @param lazyListState [LazyListState] that determines whether it is idle.
 * @param modifier [Modifier] to be applied to the underlying [Layout].
 * @param material [ButtonBarMaterial] that defines how the container will be colored.
 * @param colors [ButtonBarColors] by which it will be colored.
 * @param content [PrimaryButton] to be shown.
 */
@Composable
fun ButtonBar(
  lazyListState: LazyListState,
  modifier: Modifier = Modifier,
  material: ButtonBarMaterial = ButtonBarDefaults.Material,
  colors: ButtonBarColors = ButtonBarDefaults.colors(),
  content: @Composable () -> Unit
) {
  val isIdle by remember(lazyListState) { derivedStateOf { lazyListState.canScrollBackward } }
  ButtonBar(isIdle, modifier, material, colors, content)
}

/**
 * Bar for housing a [PrimaryButton] in an idle state.
 *
 * @param modifier [Modifier] to be applied to the underlying [Layout].
 * @param material [ButtonBarMaterial] that defines how the container will be colored.
 * @param containerColor [Color] by which the container is colored.
 * @param content [PrimaryButton] to be shown.
 */
@Composable
fun ButtonBar(
  modifier: Modifier = Modifier,
  material: ButtonBarMaterial = ButtonBarDefaults.Material,
  containerColor: Color = ButtonBarColors.defaultIdleContainer,
  content: @Composable () -> Unit
) {
  ButtonBar(isIdle = true, modifier, material, ButtonBarDefaults.colors(containerColor), content)
}

/**
 * Bar for housing a [PrimaryButton].
 *
 * @param isIdle Whether an associated lazy list is scrolled.
 * @param modifier [Modifier] to be applied to the underlying [Layout].
 * @param material [ButtonBarMaterial] that defines how the container will be colored.
 * @param colors [ButtonBarColors] by which it will be colored.
 * @param content [PrimaryButton] to be shown.
 */
@Composable
private fun ButtonBar(
  isIdle: Boolean,
  modifier: Modifier = Modifier,
  material: ButtonBarMaterial = ButtonBarDefaults.Material,
  colors: ButtonBarColors = ButtonBarDefaults.colors(),
  content: @Composable () -> Unit
) {
  val density = LocalDensity.current
  val borderStrokeWidth by
    animateDpAsState(
      if (isIdle) ((-1).dp) else AutosTheme.borders.default.asBorderStroke.width,
      label = "BorderStrokeWidth"
    )
  val spacing = ButtonBarDefaults.spacing
  val spacingInPx = remember(density, spacing) { with(density) { spacing.roundToPx() } }

  Column(modifier) {
    HorizontalDivider(Modifier.testTag(BUTTON_BAR_DIVIDER_TAG), thickness = borderStrokeWidth)

    material.Container(
      Modifier.padding(spacing).fillMaxWidth().testTag(BUTTON_BAR_TAG),
      colors.container(isIdle),
      content
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
  AutosTheme { ButtonBar(isIdle = true) }
}

/** Preview of a highlighted [ButtonBar]. */
@Composable
@MultiThemePreview
private fun HighlightedButtonBarPreview() {
  AutosTheme { ButtonBar(isIdle = false) }
}

/**
 * Bar for housing a [PrimaryButton].
 *
 * @param isIdle Whether an associated lazy list is scrolled.
 * @param modifier [Modifier] to be applied to the underlying [Layout].
 */
@Composable
private fun ButtonBar(isIdle: Boolean, modifier: Modifier = Modifier) {
  ButtonBar(isIdle, modifier) { SampleContent() }
}

/** Sample [Button]s for a [ButtonBar]. */
@Composable
private fun SampleContent() {
  PrimaryButton(onClick = {}) { Text("Primary") }
  SecondaryButton(onClick = {}) { Text("Secondary") }
}

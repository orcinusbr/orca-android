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

package com.jeanbarrossilva.orca.platform.autos.kit.action.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.action.setting.list.ChildSettings
import com.jeanbarrossilva.orca.platform.autos.kit.action.setting.list.SettingsScope
import com.jeanbarrossilva.orca.platform.autos.kit.action.setting.list.settingsPreviewContent
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/**
 * A [Setting] that holds related child [Setting]s.
 *
 * @param icon [Icon] that visually represents what it does.
 * @param label Short description of what it's for.
 * @param modifier [Modifier] to be applied to the underlying [Column].
 * @param isInitiallyExpanded Whether it is expanded by default.
 * @param content Actions to be run on the given [SettingsScope].
 */
@Composable
internal fun Group(
  icon: @Composable () -> Unit,
  label: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  isInitiallyExpanded: Boolean = false,
  shape: CornerBasedShape = SettingDefaults.shape,
  content: SettingsScope.() -> Unit
) {
  val density = LocalDensity.current
  var isExpanded by remember(isInitiallyExpanded) { mutableStateOf(isInitiallyExpanded) }
  val expansion = updateTransition(isExpanded, label = "Transition")
  val actionRotationInDegrees by
    expansion.animateFloat(label = "ActionRotationInDegrees") { if (it) 90f else 0f }

  Column(modifier) {
    BoxWithConstraints(Modifier.zIndex(1f)) {
      val width = remember(constraints.maxWidth, constraints.maxWidth::toFloat)
      val height = remember(constraints.maxHeight, constraints.maxHeight::toFloat)
      val size = remember(width, height) { Size(width, height) }
      val expandedShapeBottomEndCornerSizeInDp =
        remember(density, size, shape) {
          with(density) { shape.bottomEnd.toPx(size, density).toDp() }
        }
      val expandedShapeBottomStartCornerSizeInDp =
        remember(density, size, shape) {
          with(density) { shape.bottomStart.toPx(size, density).toDp() }
        }
      val bottomEndCornerSizeInDp by
        expansion.animateDp(label = "BottomEndCornerSizeInDp") {
          if (it) 0.dp else expandedShapeBottomEndCornerSizeInDp
        }
      val bottomStartCornerSizeInDp by
        expansion.animateDp(label = "BottomStartCornerSizeInDp") {
          if (it) 0.dp else expandedShapeBottomStartCornerSizeInDp
        }
      val bottomEndCornerSize =
        remember(bottomEndCornerSizeInDp) { CornerSize(bottomEndCornerSizeInDp) }
      val bottomStartCornerSize =
        remember(bottomStartCornerSizeInDp) { CornerSize(bottomStartCornerSizeInDp) }

      Setting(
        label,
        shape = shape.copy(bottomEnd = bottomEndCornerSize, bottomStart = bottomStartCornerSize),
        onClick = { isExpanded = !isExpanded },
        icon = icon
      ) {
        icon(contentDescription = { "Expand" }, Modifier.rotate(actionRotationInDegrees)) {
          forward.asImageVector
        }
      }
    }

    @OptIn(ExperimentalAnimationApi::class)
    expansion.AnimatedVisibility(
      visible = { it },
      Modifier.zIndex(0f),
      enter = slideInVertically { -it } + expandVertically(),
      exit = slideOutVertically { -it } + shrinkVertically()
    ) {
      ChildSettings(content = content)
    }
  }
}

/** Preview of a collapsed [Group]. */
@Composable
@MultiThemePreview
private fun CollapsedGroupPreview() {
  AutosTheme { Group(isInitiallyExpanded = false) }
}

/** Preview of an expanded [Group]. */
@Composable
@MultiThemePreview
private fun ExpandedGroupPreview() {
  AutosTheme { Group(isInitiallyExpanded = true) }
}

/**
 * A [Setting] that holds related child [Setting]s.
 *
 * @param isInitiallyExpanded Whether it is expanded by default.
 * @param modifier [Modifier] to be applied to the underlying [Column].
 */
@Composable
private fun Group(isInitiallyExpanded: Boolean, modifier: Modifier = Modifier) {
  Group(
    icon = {
      Icon(AutosTheme.iconography.home.filled.asImageVector, contentDescription = "Setting")
    },
    label = { Text("Group") },
    modifier,
    isInitiallyExpanded,
    content = settingsPreviewContent
  )
}

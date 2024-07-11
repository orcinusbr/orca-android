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

package br.com.orcinus.orca.platform.autos.kit.action.setting.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import br.com.orcinus.orca.platform.autos.border
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.action.setting.Setting
import br.com.orcinus.orca.platform.autos.kit.action.setting.SettingDefaults
import br.com.orcinus.orca.platform.autos.kit.bottom
import br.com.orcinus.orca.platform.autos.kit.top
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/** Content of [Settings] shown in previews. */
internal val settingsPreviewContent: SettingsScope.() -> Unit = {
  repeat(3) {
    setting(
      onClick = {},
      label = { Text("Label #$it") },
      icon = { Icon(AutosTheme.iconography.link.asImageVector, contentDescription = "Setting") }
    ) {
      icon(contentDescription = { "Navigate" }, vector = { forward.asImageVector })
    }
  }
}

/**
 * [Column] of [Setting]s that are shaped according to their position.
 *
 * @param modifier [Modifier] to be applied to the underlying [Column].
 * @param content Actions to be run on the given [SettingsScope].
 */
@Composable
fun Settings(modifier: Modifier = Modifier, content: SettingsScope.() -> Unit) {
  Settings(
    settingModifier = Modifier,
    singleSettingShape = SettingDefaults.shape,
    firstSettingFollowedByOthersShape = SettingDefaults.shape.top,
    modifier,
    content = content
  )
}

/**
 * [Column] of child [Setting]s that are shaped according to their position.
 *
 * @param modifier [Modifier] to be applied to the underlying [Column].
 * @param content Actions to be run on the given [SettingsScope].
 */
@Composable
internal fun ChildSettings(modifier: Modifier = Modifier, content: SettingsScope.() -> Unit) {
  Settings(
    settingModifier = Modifier.padding(start = SettingDefaults.spacing),
    singleSettingShape = SettingDefaults.shape.bottom,
    firstSettingFollowedByOthersShape = RectangleShape,
    modifier,
    content = content
  )
}

/**
 * [Column] of [Setting]s that are shaped according to their position.
 *
 * @param settingModifier [Modifier] to be applied to each [Setting].
 * @param singleSettingShape [Shape] by which the single [Setting] will be shaped.
 * @param firstSettingFollowedByOthersShape [Shape] by which the first [Setting] will be shaped when
 *   it's followed by other ones.
 * @param modifier [Modifier] to be applied to the underlying [Column].
 * @param content Actions to be run on the given [SettingsScope].
 */
@Composable
private fun Settings(
  @Suppress("ModifierParameter") settingModifier: Modifier,
  singleSettingShape: Shape,
  firstSettingFollowedByOthersShape: Shape,
  modifier: Modifier = Modifier,
  content: SettingsScope.() -> Unit
) {
  val defaultOptionShape = SettingDefaults.shape
  val scope = remember(content) { SettingsScope().apply(content) }

  Column(modifier.border(defaultOptionShape)) {
    scope.settings.forEachIndexed { index, setting ->
      when {
        scope.settings.size == 1 -> setting(settingModifier, singleSettingShape)
        index == 0 -> setting(settingModifier, firstSettingFollowedByOthersShape)
        index == scope.settings.lastIndex -> setting(settingModifier, defaultOptionShape.bottom)
        else -> setting(settingModifier, RectangleShape)
      }

      if (index != scope.settings.lastIndex) {
        HorizontalDivider()
      }
    }
  }
}

/** Preview of parent [Settings]. */
@Composable
@MultiThemePreview
private fun ParentSettingsPreview() {
  AutosTheme { Settings(content = settingsPreviewContent) }
}

/** Preview of child [Settings]. */
@Composable
@MultiThemePreview
private fun ChildSettingsPreview() {
  AutosTheme { ChildSettings(content = settingsPreviewContent) }
}

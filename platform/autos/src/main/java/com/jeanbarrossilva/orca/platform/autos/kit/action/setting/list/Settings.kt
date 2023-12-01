/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.platform.autos.kit.action.setting.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.jeanbarrossilva.orca.platform.autos.border
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.action.setting.Setting
import com.jeanbarrossilva.orca.platform.autos.kit.action.setting.SettingDefaults
import com.jeanbarrossilva.orca.platform.autos.kit.bottom
import com.jeanbarrossilva.orca.platform.autos.kit.top
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

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
        Divider()
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

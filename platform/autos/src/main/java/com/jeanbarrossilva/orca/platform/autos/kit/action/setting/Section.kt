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

package com.jeanbarrossilva.orca.platform.autos.kit.action.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.kit.action.setting.list.Settings
import com.jeanbarrossilva.orca.platform.autos.kit.action.setting.list.SettingsScope
import com.jeanbarrossilva.orca.platform.autos.kit.action.setting.list.settingsPreviewContent
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

@Composable
fun Section(title: String, modifier: Modifier = Modifier, content: SettingsScope.() -> Unit) {
  Column(modifier, verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.small.dp)) {
    Text(
      title.uppercase(),
      Modifier.offset(x = SettingDefaults.spacing),
      style = AutosTheme.typography.labelMedium
    )

    Settings(modifier, content)
  }
}

@Composable
@MultiThemePreview
private fun SectionPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      Section(title = "Section", content = settingsPreviewContent)
    }
  }
}

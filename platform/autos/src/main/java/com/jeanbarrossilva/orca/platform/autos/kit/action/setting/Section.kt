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

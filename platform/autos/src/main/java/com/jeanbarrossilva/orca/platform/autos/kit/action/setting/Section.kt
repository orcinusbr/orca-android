package com.jeanbarrossilva.orca.platform.autos.kit.action.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.autos.autos.colors.asColor
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

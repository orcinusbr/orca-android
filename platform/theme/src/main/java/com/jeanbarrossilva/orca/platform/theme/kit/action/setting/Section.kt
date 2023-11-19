package com.jeanbarrossilva.orca.platform.theme.kit.action.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.theme.kit.action.setting.list.Settings
import com.jeanbarrossilva.orca.platform.theme.kit.action.setting.list.SettingsScope
import com.jeanbarrossilva.orca.platform.theme.kit.action.setting.list.settingsPreviewContent

@Composable
fun Section(title: String, modifier: Modifier = Modifier, content: SettingsScope.() -> Unit) {
  Column(modifier, verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.small.dp)) {
    Text(
      title.uppercase(),
      Modifier.offset(x = SettingDefaults.spacing),
      style = OrcaTheme.typography.labelMedium
    )

    Settings(modifier, content)
  }
}

@Composable
@MultiThemePreview
private fun SectionPreview() {
  OrcaTheme {
    Surface(color = OrcaTheme.colors.background.container.asColor) {
      Section(title = "Section", content = settingsPreviewContent)
    }
  }
}

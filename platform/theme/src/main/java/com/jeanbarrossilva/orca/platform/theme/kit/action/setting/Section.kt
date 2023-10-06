package com.jeanbarrossilva.orca.platform.theme.kit.action.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.kit.action.setting.list.Settings
import com.jeanbarrossilva.orca.platform.theme.kit.action.setting.list.SettingsScope
import com.jeanbarrossilva.orca.platform.theme.kit.action.setting.list.settingsPreviewContent

@Composable
fun Section(title: String, modifier: Modifier = Modifier, content: SettingsScope.() -> Unit) {
    Column(
        modifier.padding(top = OrcaTheme.spacings.medium),
        verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.small)
    ) {
        Text(title.uppercase(), style = OrcaTheme.typography.titleSmall)
        Settings(modifier, content)
    }
}

@Composable
@MultiThemePreview
private fun SectionPreview() {
    OrcaTheme {
        Section(title = "Section", content = settingsPreviewContent)
    }
}

package com.jeanbarrossilva.orca.feature.settings

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.kit.action.setting.list.SettingsScope

/**
 * Adds a muting setting.
 *
 * @param mutedTerms Terms that have been muted.
 * @param onNavigationToTermMuting Lambda that's invoked when navigation to term muting settings is
 * requested to be performed.
 * @param onUnmute Callback run whenever one of the [mutedTerms] is requested to be unmuted.
 **/
internal fun SettingsScope.muting(
    mutedTerms: List<String>,
    onNavigationToTermMuting: () -> Unit,
    onUnmute: (term: String) -> Unit
) {
    group(
        icon = { Icon(OrcaTheme.iconography.mute.filled, contentDescription = "Muting") },
        label = { Text("Muting") }
    ) {
        setting(
            onClick = onNavigationToTermMuting,
            label = { Text("Add") },
            icon = { Icon(OrcaTheme.iconography.add, contentDescription = "Add") }
        )
        mutedTerms.forEach {
            setting(label = { Text(it) }) {
                button(contentDescription = "Remove", onClick = { onUnmute(it) }) {
                    delete.filled
                }
            }
        }
    }
}

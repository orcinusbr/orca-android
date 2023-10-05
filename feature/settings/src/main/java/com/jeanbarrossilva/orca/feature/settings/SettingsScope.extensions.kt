package com.jeanbarrossilva.orca.feature.settings

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.kit.action.setting.list.SettingsScope

/**
 * Adds a muting setting.
 *
 * @param mutedTerms Terms that have been muted.
 * @param onUnmute Callback run whenever one of the [mutedTerms] is requested to be unmuted.
 **/
internal fun SettingsScope.muting(
    mutedTerms: List<String>,
    onUnmute: (term: String) -> Unit
) {
    group(
        icon = { Icon(OrcaTheme.iconography.mute.filled, contentDescription = "Muting") },
        label = { Text("Muting") }
    ) {
        setting(
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

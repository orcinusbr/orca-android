package com.jeanbarrossilva.orca.feature.settings

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.action.setting.list.SettingsScope
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme

/**
 * Adds a muting setting.
 *
 * @param mutedTerms Terms that have been muted.
 * @param onNavigationToTermMuting Lambda that's invoked when navigation to term muting settings is
 *   requested to be performed.
 * @param onUnmute Callback run whenever one of the [mutedTerms] is requested to be unmuted.
 */
internal fun SettingsScope.muting(
  mutedTerms: List<String>,
  onNavigationToTermMuting: () -> Unit,
  onUnmute: (term: String) -> Unit
) {
  group(
    icon = {
      Icon(
        AutosTheme.iconography.mute.filled.asImageVector,
        contentDescription = stringResource(R.string.feature_settings_muting)
      )
    },
    label = { Text(stringResource(R.string.feature_settings_muting)) }
  ) {
    setting(
      onClick = onNavigationToTermMuting,
      label = { Text(stringResource(R.string.feature_settings_add)) },
      icon = {
        Icon(
          AutosTheme.iconography.add.asImageVector,
          contentDescription = stringResource(R.string.feature_settings_add)
        )
      }
    )
    mutedTerms.forEach {
      setting(label = { Text(it) }) {
        button(
          contentDescription = { stringResource(R.string.feature_settings_remove) },
          onClick = { onUnmute(it) }
        ) {
          delete.filled.asImageVector
        }
      }
    }
  }
}

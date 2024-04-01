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

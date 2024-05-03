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

package br.com.orcinus.orca.feature.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.platform.autos.kit.action.setting.Section
import br.com.orcinus.orca.platform.autos.kit.scaffold.Scaffold
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.TopAppBar
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.TopAppBarDefaults
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText
import br.com.orcinus.orca.platform.autos.kit.scaffold.plus
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

@Composable
internal fun Settings(
  viewModel: SettingsViewModel,
  boundary: SettingsBoundary,
  modifier: Modifier = Modifier
) {
  val mutedTerms by viewModel.mutedTermsFlow.collectAsState()

  Settings(
    mutedTerms,
    onNavigationToTermMuting = boundary::navigateToTermMuting,
    onTermUnmute = viewModel::unmute,
    modifier
  )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Settings(
  mutedTerms: List<String>,
  onNavigationToTermMuting: () -> Unit,
  onTermUnmute: (term: String) -> Unit,
  modifier: Modifier = Modifier
) {
  val topAppBarScrollBehavior = TopAppBarDefaults.scrollBehavior
  val lazyListState = rememberLazyListState()

  Scaffold(
    modifier,
    topAppBar = {
      TopAppBar(
        title = { AutoSizeText(stringResource(R.string.feature_settings)) },
        scrollBehavior = topAppBarScrollBehavior
      )
    }
  ) {
    navigable {
      LazyColumn(
        Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        state = lazyListState,
        contentPadding = PaddingValues(AutosTheme.spacings.medium.dp)
      ) {
        item {
          Section(stringResource(R.string.feature_settings_general)) {
            muting(mutedTerms, onNavigationToTermMuting, onTermUnmute)
          }
        }
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun SettingsPreview() {
  AutosTheme {
    Settings(mutedTerms = listOf("Java"), onNavigationToTermMuting = {}, onTermUnmute = {})
  }
}

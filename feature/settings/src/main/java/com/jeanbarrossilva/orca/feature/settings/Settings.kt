package com.jeanbarrossilva.orca.feature.settings

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
import com.jeanbarrossilva.orca.platform.autos.kit.action.setting.Section
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.TopAppBar
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.TopAppBarDefaults
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.plus
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

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
    LazyColumn(
      Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
      state = lazyListState,
      contentPadding = it + PaddingValues(AutosTheme.spacings.medium.dp)
    ) {
      item {
        Section(stringResource(R.string.feature_settings_general)) {
          muting(mutedTerms, onNavigationToTermMuting, onTermUnmute)
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

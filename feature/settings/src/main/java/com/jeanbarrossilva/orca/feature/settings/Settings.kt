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
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.extensions.plus
import com.jeanbarrossilva.orca.platform.theme.kit.action.setting.Section
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.TopAppBar
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.TopAppBarDefaults
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.text.AutoSizeText

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
        title = { AutoSizeText(stringResource(R.string.settings)) },
        scrollBehavior = topAppBarScrollBehavior
      )
    }
  ) {
    LazyColumn(
      Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
      state = lazyListState,
      contentPadding = it + PaddingValues(OrcaTheme.spacings.medium)
    ) {
      item {
        Section(stringResource(R.string.settings_general)) {
          muting(mutedTerms, onNavigationToTermMuting, onTermUnmute)
        }
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun SettingsPreview() {
  OrcaTheme {
    Settings(mutedTerms = listOf("Java"), onNavigationToTermMuting = {}, onTermUnmute = {})
  }
}

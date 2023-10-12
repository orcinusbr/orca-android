package com.jeanbarrossilva.orca.feature.settings.termmuting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.extensions.plus
import com.jeanbarrossilva.orca.platform.theme.kit.action.button.PrimaryButton
import com.jeanbarrossilva.orca.platform.theme.kit.input.TextField
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.button.ButtonBar
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.TopAppBarDefaults
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.TopAppBarWithBackNavigation
import com.jeanbarrossilva.orca.platform.ui.core.requestFocusWithDelay

@Composable
internal fun TermMuting(
  viewModel: TermMutingViewModel,
  boundary: TermMutingBoundary,
  modifier: Modifier = Modifier
) {
  val term by viewModel.termFlow.collectAsState()

  TermMuting(
    term,
    onTermChange = viewModel::setTerm,
    onMute = viewModel::mute,
    onPop = boundary::pop,
    modifier
  )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TermMuting(
  term: String,
  onTermChange: (term: String) -> Unit,
  onMute: () -> Unit,
  onPop: () -> Unit,
  modifier: Modifier = Modifier
) {
  val topAppBarScrollBehavior = TopAppBarDefaults.scrollBehavior
  val lazyListState = rememberLazyListState()
  val spacing = OrcaTheme.spacings.medium
  val focusRequester = remember(::FocusRequester)
  val muteAndPop by rememberUpdatedState {
    onMute()
    onPop()
  }

  LaunchedEffect(Unit) { focusRequester.requestFocusWithDelay() }

  Scaffold(
    modifier,
    topAppBar = {
      TopAppBarWithBackNavigation(
        onNavigation = onPop,
        title = { Text(stringResource(R.string.settings_term_muting)) },
        subtitle = { Text(stringResource(R.string.settings_term_muting_settings)) },
        scrollBehavior = topAppBarScrollBehavior
      )
    },
    buttonBar = {
      ButtonBar(lazyListState) {
        PrimaryButton(onClick = muteAndPop) {
          Text(stringResource(R.string.settings_term_muting_mute))
        }
      }
    }
  ) {
    LazyColumn(
      Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
      lazyListState,
      verticalArrangement = Arrangement.spacedBy(spacing),
      contentPadding = it + PaddingValues(spacing)
    ) {
      item {
        TextField(
          term,
          onTermChange,
          Modifier.focusRequester(focusRequester).fillMaxWidth(),
          KeyboardOptions(imeAction = ImeAction.Done),
          KeyboardActions(onDone = { muteAndPop() })
        ) {
          Text(stringResource(R.string.settings_term_muting_term))
        }
      }

      item {
        Text(
          stringResource(R.string.settings_term_muting_explanation),
          style = OrcaTheme.typography.bodySmall
        )
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun TermMutingPreview() {
  OrcaTheme { TermMuting(term = "🐛", onTermChange = {}, onMute = {}, onPop = {}) }
}

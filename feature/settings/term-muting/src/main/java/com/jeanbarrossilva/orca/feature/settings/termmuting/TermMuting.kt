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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.extensions.plus
import com.jeanbarrossilva.orca.platform.theme.kit.action.button.PrimaryButton
import com.jeanbarrossilva.orca.platform.theme.kit.input.text.TextField
import com.jeanbarrossilva.orca.platform.theme.kit.input.text.error.containsErrorsAsState
import com.jeanbarrossilva.orca.platform.theme.kit.input.text.error.rememberErrorDispatcher
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.button.ButtonBar
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.TopAppBarDefaults
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.TopAppBarWithBackNavigation
import com.jeanbarrossilva.orca.platform.ui.core.requestFocusWithDelay

internal const val SETTINGS_TERM_MUTING_TEXT_FIELD_TAG = "settings-term-muting-text-field"
internal const val SETTINGS_TERM_MUTING_MUTE_BUTTON = "settings-term-muting-mute-button"

@Composable
internal fun TermMuting(
  viewModel: TermMutingViewModel,
  boundary: TermMutingBoundary,
  modifier: Modifier = Modifier
) {
  val term by viewModel.termFlow.collectAsState()

  TermMuting(
    modifier,
    term,
    onTermChange = viewModel::setTerm,
    onMute = viewModel::mute,
    onPop = boundary::pop
  )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun TermMuting(
  modifier: Modifier = Modifier,
  term: String,
  onTermChange: (term: String) -> Unit,
  onMute: () -> Unit,
  onPop: () -> Unit,
) {
  val context = LocalContext.current
  val topAppBarScrollBehavior = TopAppBarDefaults.scrollBehavior
  val lazyListState = rememberLazyListState()
  val spacing = OrcaTheme.spacings.medium
  val focusRequester = remember(::FocusRequester)
  val errorDispatcher = rememberErrorDispatcher {
    error(context, R.string.settings_term_muting_empty_error, String::isBlank)
  }
  val containsErrors by errorDispatcher.containsErrorsAsState
  val onDone by rememberUpdatedState {
    errorDispatcher.dispatch()
    if (!containsErrors) {
      onMute()
      onPop()
    }
  }

  LaunchedEffect(Unit) { focusRequester.requestFocusWithDelay() }

  DisposableEffect(term) {
    errorDispatcher.register(term)
    onDispose {}
  }

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
        PrimaryButton(onClick = onDone, Modifier.testTag(SETTINGS_TERM_MUTING_MUTE_BUTTON)) {
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
          Modifier.focusRequester(focusRequester)
            .fillMaxWidth()
            .testTag(SETTINGS_TERM_MUTING_TEXT_FIELD_TAG),
          errorDispatcher,
          KeyboardOptions(imeAction = ImeAction.Done),
          KeyboardActions(onDone = { onDone() })
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

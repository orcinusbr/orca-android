/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.PrimaryButton
import com.jeanbarrossilva.orca.platform.autos.kit.input.text.TextField
import com.jeanbarrossilva.orca.platform.autos.kit.input.text.error.containsErrorsAsState
import com.jeanbarrossilva.orca.platform.autos.kit.input.text.error.rememberErrorDispatcher
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.ButtonBar
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.TopAppBarDefaults
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.TopAppBarWithBackNavigation
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.plus
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.core.requestFocusWithDelay

internal const val SETTINGS_TERM_MUTING_TEXT_FIELD_TAG = "settings-term-muting-text-field"
internal const val SETTINGS_TERM_MUTING_MUTE_BUTTON = "settings-term-muting-mute-button"

@Composable
internal fun TermMuting(
  viewModel: TermMutingViewModel,
  onBackwardsNavigation: () -> Unit,
  modifier: Modifier = Modifier
) {
  val term by viewModel.termFlow.collectAsState()

  TermMuting(
    modifier,
    term,
    onTermChange = viewModel::setTerm,
    onMute = viewModel::mute,
    onBackwardsNavigation
  )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun TermMuting(
  modifier: Modifier = Modifier,
  term: String,
  onTermChange: (term: String) -> Unit,
  onMute: () -> Unit,
  onBackwardsNavigation: () -> Unit,
) {
  val context = LocalContext.current
  val topAppBarScrollBehavior = TopAppBarDefaults.scrollBehavior
  val lazyListState = rememberLazyListState()
  val spacing = AutosTheme.spacings.medium.dp
  val focusRequester = remember(::FocusRequester)
  val errorDispatcher = rememberErrorDispatcher {
    error(context, R.string.feature_settings_term_muting_empty_error, String::isBlank)
  }
  val containsErrors by errorDispatcher.containsErrorsAsState
  val onDone by rememberUpdatedState {
    errorDispatcher.dispatch()
    if (!containsErrors) {
      onMute()
      onBackwardsNavigation()
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
        onBackwardsNavigation,
        title = { Text(stringResource(R.string.feature_settings_term_muting)) },
        subtitle = { Text(stringResource(R.string.feature_settings_term_muting_settings)) },
        scrollBehavior = topAppBarScrollBehavior
      )
    },
    buttonBar = {
      ButtonBar(lazyListState) {
        PrimaryButton(onClick = onDone, Modifier.testTag(SETTINGS_TERM_MUTING_MUTE_BUTTON)) {
          Text(stringResource(R.string.feature_settings_term_muting_mute))
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
          Text(stringResource(R.string.feature_settings_term_muting_term))
        }
      }

      item {
        Text(
          stringResource(R.string.feature_settings_term_muting_explanation),
          style = AutosTheme.typography.bodySmall
        )
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun TermMutingPreview() {
  AutosTheme { TermMuting(term = "🐛", onTermChange = {}, onMute = {}, onBackwardsNavigation = {}) }
}

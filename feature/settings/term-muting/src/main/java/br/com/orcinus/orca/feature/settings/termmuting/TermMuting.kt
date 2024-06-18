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

package br.com.orcinus.orca.feature.settings.termmuting

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.platform.autos.kit.action.button.PrimaryButton
import br.com.orcinus.orca.platform.autos.kit.input.text.FormTextField
import br.com.orcinus.orca.platform.autos.kit.input.text.error.containsErrorsAsState
import br.com.orcinus.orca.platform.autos.kit.input.text.error.rememberErrorDispatcher
import br.com.orcinus.orca.platform.autos.kit.scaffold.Scaffold
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.button.ButtonBar
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.TopAppBarDefaults
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.TopAppBarWithBackNavigation
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.focus.rememberImmediateFocusRequester

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
internal fun TermMuting(modifier: Modifier = Modifier) {
  TermMuting(modifier, term = "", onTermChange = {}, onMute = {}, onBackwardsNavigation = {})
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TermMuting(
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
  val focusRequester = rememberImmediateFocusRequester()
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
    bottom = {
      ButtonBar(lazyListState) {
        PrimaryButton(onClick = onDone, Modifier.testTag(SETTINGS_TERM_MUTING_MUTE_BUTTON)) {
          Text(stringResource(R.string.feature_settings_term_muting_mute))
        }
      }
    }
  ) {
    navigable {
      LazyColumn(
        Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        lazyListState,
        verticalArrangement = Arrangement.spacedBy(spacing),
        contentPadding = PaddingValues(spacing)
      ) {
        item {
          FormTextField(
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
}

@Composable
@MultiThemePreview
private fun TermMutingPreview() {
  AutosTheme { TermMuting() }
}
